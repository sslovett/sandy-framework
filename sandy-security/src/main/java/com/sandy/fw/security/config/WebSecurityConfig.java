package com.sandy.fw.security.config;

import com.sandy.fw.security.exception.AccessDeniedExceptionHandler;
import com.sandy.fw.security.exception.AuthenticationExceptionHandler;
import com.sandy.fw.security.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthFilter authFilter;

    @Autowired
    AuthenticationExceptionHandler authenticationExceptionHandler;

    @Autowired
    AccessDeniedExceptionHandler accessDeniedExceptionHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//csrf针对的是session的攻击，使用jwt就不需要这个保护
                //基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);//添加token校验filter
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationExceptionHandler)//添加认证失败处理
                .accessDeniedHandler(accessDeniedExceptionHandler);//添加授权失败处理


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/login");
    }
}
