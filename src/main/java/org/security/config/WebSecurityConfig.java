package org.security.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 *  springSecurity 核心配置文件
 *
 *  @Author Joey Li
 *  @Data 2018-05-23
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	protected UserDetailsService customUserService;

	@Autowired
	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
				// 设置UserDetailsService
				.userDetailsService(customUserService)
				// 使用BCrypt进行密码的hash
				.passwordEncoder(passwordEncoder());
	}
	/**
     *
     * 装载BCrypt密码编码器
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// 由于使用的是JWT，我们这里不需要csrf
				.csrf().disable()
				// 基于token，所以不需要session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				//.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				// 允许对于网站静态资源的无授权访问
				.antMatchers(
						HttpMethod.GET,
						"/",
						"/*.html",
						"/swagger-resources/**",
						"/v2/**",
						"/favicon.ico",
						"/**/*.html",
						"/**/*.png",
						"/**/*.ttf",
						"/**/*.css",
						"/**/*.js"
				).permitAll()
				// 对于获取token的rest api要允许匿名访问
				.antMatchers("/**").permitAll()
				// 除上面外的所有请求全部需要鉴权认证
				.anyRequest().authenticated();
		httpSecurity.headers().cacheControl();
	}
}
