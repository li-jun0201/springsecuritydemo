package org.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *  SWAGGER2 核心配置文件
 *
 *  @Author Joey Li
 *  @Data 2018-05-23
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    /*@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }*/
}
