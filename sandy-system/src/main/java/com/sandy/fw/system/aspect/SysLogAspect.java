package com.sandy.fw.system.aspect;

import com.alibaba.fastjson.JSON;
import com.sandy.fw.system.models.SysLog;
import com.sandy.fw.system.service.SysLogService;
import com.sandy.fw.common.annotation.Log;
import com.sandy.fw.common.util.IpHelper;
import com.sandy.fw.security.util.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        SysLog sysLog = new SysLog();
        if(log != null) {
            //注解上的描述
            sysLog.setOperation(log.value());
        }
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        sysLog.setMethod(className + "." + methodName + "()");
        //请求参数
        Object[] args = joinPoint.getArgs();
        sysLog.setParams(args.length == 0 ? "" : JSON.toJSONString(args));
        //请求IP(考虑多网卡)
        sysLog.setIp(IpHelper.getLocalIpAddress());
        //用户姓名
        sysLog.setUsername(SecurityUtils.getUserInfo().getUsername());
        sysLog.setTime(endTime - startTime);
        sysLog.setCreateDate(new Date());
        //保存系统日志
        sysLogService.save(sysLog);

        return result;
    }
}
