package com.sandy.fw.security.filter;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandy.fw.common.exception.DefaultException;
import com.sandy.fw.common.response.ResponseEnum;
import com.sandy.fw.common.response.ServerResponseEntity;
import com.sandy.fw.common.util.ResponsePrintUtil;
import com.sandy.fw.security.bean.UserInfoToken;
import com.sandy.fw.security.token.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    @Value("${jwt.token-name}")
    String tokenName;

    @Autowired
    private TokenManager tokenManager;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String accessToken = request.getHeader(tokenName);
        try {
            if(StrUtil.isEmpty(accessToken)) {
                //可能是登录接口或未携带token的非法请求，交给spring-security处理
                chain.doFilter(request, response);
                return;
            }
            UserInfoToken userInfoToken = null;
            //检查登录token
            try {
                userInfoToken = tokenManager.checkLogin(accessToken);
            }catch (Exception ex) {
                log.error("请求地址：{"+ request.getRequestURI() +"}, 登录认证失败", ex);
                ResponsePrintUtil.print(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
                return;
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userInfoToken,null, userInfoToken.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            chain.doFilter(request, response);

        } catch (Exception e) {
            if(e instanceof DefaultException) {
                DefaultException ex = (DefaultException) e;
                log.error("请求地址：{"+ request.getRequestURI() +"}, " + ex.getMessage(), ex);
                ResponsePrintUtil.print(ServerResponseEntity.fail(ex.getCode(), ex.getMessage()));
            } else {
                log.error("请求地址：{"+ request.getRequestURI() +"}, 系统异常", e);
                ResponsePrintUtil.print(ServerResponseEntity.fail(ResponseEnum.PARAM_ERROR));
            }
        }

    }


}
