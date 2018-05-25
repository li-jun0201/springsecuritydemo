package org.security.config;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.security.service.RedisService;
import org.security.util.JwtTokenUtil;
import org.security.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 *  JWT 核心拦截器
 *
 *  @Author Joey Li
 *  @Data 2018-05-23
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${server.api.switch}")
    private String flag;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);
        boolean breakFilter = false;
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            logger.info("checking authentication " + username);
            if (username != null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    logger.info("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else{
                    generateJsonResponse(response,false,"your token has expired!");
                    breakFilter = true;
                }
            }else{
                breakFilter = true;
                generateJsonResponse(response,true,"your token is invalid!");
            }
        }else{
            if("1".equals(redisService.getValue(flag))){
                if(request.getRequestURI().contains("api") && !request.getRequestURI().contains("docs")){
                    breakFilter = true;
                    generateJsonResponse(response,true,"your token is missing or wrong token pattern!");
                }
            }
        }
        if(!breakFilter) {
            chain.doFilter(request, response);
        }
    }

    private void generateJsonResponse(HttpServletResponse response,boolean isError, String msg) {
        response.setContentType("application/json; charset=utf-8");
        OutputStream out = null;
        Result result = Result.errorResult();
        try {
            out = response.getOutputStream();
            if (!isError) {   //token
                result.setCode(1001);
            }
            result.setMsg(msg);
            out.write(result.toString().getBytes("utf-8"));
        }catch(Exception e){
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }finally{
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断 是否为swagger-ui 页面的请求
     * author joey li
     * @param request
     * @return
     */
    private boolean isSwaggerRequest(HttpServletRequest request){
        String uri = request.getRequestURI();
        if(uri==null)
            return false;
        return uri.contains("api-docs")||uri.contains("configuration/ui")||uri.contains("swagger-resources");

    }

   /* @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            logger.info("checking authentication " + username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                	if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                                request));
                        logger.info("authenticated user " + username + ", setting security context");
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
            }
        }
        chain.doFilter(request, response);
    }*/
}

