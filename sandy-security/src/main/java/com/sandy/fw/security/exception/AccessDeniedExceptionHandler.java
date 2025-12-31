package com.sandy.fw.security.exception;

import com.sandy.fw.common.response.ResponseEnum;
import com.sandy.fw.common.response.ServerResponseEntity;
import com.sandy.fw.common.util.ResponsePrintUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {
        ResponsePrintUtil.print(ServerResponseEntity.fail(ResponseEnum.UN_ACCESS_DENIED));
    }
}
