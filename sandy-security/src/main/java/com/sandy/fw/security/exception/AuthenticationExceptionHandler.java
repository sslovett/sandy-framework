package com.sandy.fw.security.exception;

import com.sandy.fw.common.response.ResponseEnum;
import com.sandy.fw.common.response.ServerResponseEntity;
import com.sandy.fw.common.util.ResponsePrintUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理认证异常
 */
@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        ResponsePrintUtil.print(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
    }
}
