package com.sandy.fw.security.token;

import com.alibaba.fastjson.JSON;
import com.sandy.fw.common.exception.DefaultException;
import com.sandy.fw.common.response.ResponseEnum;
import com.sandy.fw.common.util.RedisUtil;
import com.sandy.fw.security.bean.UserInfoToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class TokenManager {

    @Autowired
    RedisUtil redisUtil;

    @Value("${jwt.timeout}")
    private int timeoutSecond;

    private static final String USER_INFO = "admin_info:";

    public String login(UserInfoToken userInfo) {
        String token = JwtUtil.createJWT(userInfo.getUserId(), timeoutSecond * 1000L);
        String keyName = USER_INFO + token;
        redisUtil.del(keyName);
        redisUtil.set(keyName, JSON.toJSONString(userInfo), timeoutSecond);

        return token;
    }

    public UserInfoToken checkLogin(String token) {
        //验证token有效性
        if(!JwtUtil.verify(token)){
            log.error("token验证失败");
            throw new DefaultException(ResponseEnum.UNAUTHORIZED);
        }
        //验证redis保存的登录用户信息是否已过期
        String keyName = USER_INFO + token;

        Object redisCache = redisUtil.get(keyName);
        if(null==redisCache) {
            log.error("redis中登录用户信息已过期");
            throw new DefaultException(ResponseEnum.UNAUTHORIZED);
        }
        return JSON.parseObject(redisCache.toString(), UserInfoToken.class);
    }




}
