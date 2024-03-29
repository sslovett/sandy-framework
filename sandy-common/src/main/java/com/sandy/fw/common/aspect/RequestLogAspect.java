package com.sandy.fw.common.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class RequestLogAspect {

    /**
     * 切点路径：Controller层的所有方法
     */
    @Pointcut("execution(public * com.sandy.fw.*.controller.*.*(..))")
    public void webLog() {}

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object result = proceedingJoinPoint.proceed();
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUrl(request.getRequestURL().toString());
        requestInfo.setIp(request.getRemoteAddr());
        requestInfo.setHttpMethod(request.getMethod());
        requestInfo.setClassMethod(proceedingJoinPoint.getSignature().getDeclaringTypeName() + "."
                + proceedingJoinPoint.getSignature().getName());
        requestInfo.setRequestParams(JSON.toJSONString(proceedingJoinPoint.getArgs()));
        requestInfo.setTimeCost(System.currentTimeMillis() - startTime);
        requestInfo.setResult(truncateString(JSON.toJSONString(result)));
        requestInfo.print();//记录日志
        return result;
    }

    private String truncateString(String s) {
        if (s.length() < 200) {
            return s;
        }
        return s.substring(0, 200) + "...";
    }
}
