package org.security.controller;

import io.swagger.annotations.Api;
import org.security.entity.Msg;
import org.security.util.MyUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *  demo controller
 *
 *  @Author Joey Li
 *  @Data 2018-05-23
 */
@RestController
@Api(value="Home",description="接口",tags="Test")
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {
        Msg msg = new Msg("测试标题", "测试内容", "额外信息，只对管理员显示");
        model.addAttribute("msg", msg);
        return "index";
    }
    
    @PreAuthorize(MyUtils.USER_AUTH)
    @GetMapping(value="/auto/user")
    public String adminTest1() {
        return "ROLE_USER";
    }

    @GetMapping("/auto/admin")
    @PreAuthorize(MyUtils.ADMIN_AUTH)
    public String adminTest2() {
        return "ROLE_ADMIN";
    }

    @GetMapping("/auto/all")
    @PreAuthorize(MyUtils.USER_ADMIN_AUTH)
    public String adminTest3() {
        return "ROle_user +  ROLE_ADMIN";
    }
}
