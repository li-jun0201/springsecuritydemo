package org.security.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.security.dao.AuthService;
import org.security.entity.SysUser;
import org.security.service.JwtAuthenticationRequest;
import org.security.service.JwtAuthenticationResponse;
import org.security.service.RedisService;
import org.security.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 *  auth controller
 *
 *  @Author Joey Li
 *  @Data 2018-05-23
 */
@RestController
@Slf4j
@Api(value="Auth",description="权限认证",tags="Auth")
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;
    @Autowired
    private AuthService authService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserDetailsService userDetailsService;

    @ApiOperation(value="登陆接口",notes="login")
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public Result createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest
    ) throws AuthenticationException{
        Result result = Result.successResult();
        try {
            userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        }catch(Exception e ){
            return failResult("用户不存在或者已经被停用！");
        }
        try{
            final  String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            redisService.setToken(token);
log.debug(String.format("[User login success] [userName:%s,token:%s]", authenticationRequest.getUsername(),token));
            Map<String, String> map = new HashMap<String,String>();
            map.put("token",token);
            result.setObj(map);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            return failResult("用户名密码不匹配！");
        }
    }

    @RequestMapping(value = "/auth/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }

    @RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
    public Result register(@RequestBody SysUser addedUser) throws AuthenticationException{
        Result result = Result.successResult();
        String token = authService.register(addedUser);
        if(token==null){
            return failResult("用户名已经存在！");
        }else{
            Map<String, String> map = new HashMap<String,String>();
            map.put("token",token);
            redisService.setToken(token);

            result.setObj(map);
        }
        return result;
    }

    private Result failResult(String msg){
        Result result = Result.errorResult();
        result.setMsg(msg);
        return result;
    }
}
