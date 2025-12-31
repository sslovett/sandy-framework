package com.sandy.fw.security.token;

import cn.hutool.core.util.StrUtil;
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

    @Value("${jwt.powerful-token}")
    private String powerfulToken;

    private static final String USER_INFO = "admin_info:";

    public String login(UserInfoToken userInfo) {
        String token = JwtUtil.createJWT(userInfo.getUserId().toString(), timeoutSecond * 1000L);
        userInfo.setToken(token);
        String keyName = USER_INFO + token;
        redisUtil.del(keyName);
        redisUtil.set(keyName, JSON.toJSONString(userInfo), timeoutSecond);

        return token;
    }

    public void logout(String token) {
        String keyName = USER_INFO + token;
        redisUtil.del(keyName);
    }

    public UserInfoToken checkLogin(String token) {
        //验证token有效性
        if(!isPowerfulToken(token) && !JwtUtil.verify(token)) {
            log.error("token校验失败");
            throw new DefaultException("token校验失败");
        }
        //验证redis保存的登录用户信息是否已过期
        String keyName = USER_INFO + token;

        Object redisCache = redisUtil.get(keyName);
        if(null==redisCache) {
            throw new DefaultException("登录用户信息已过期");
        }
        return JSON.parseObject(redisCache.toString(), UserInfoToken.class);
    }

    //校验是否是万能token
    private boolean isPowerfulToken(String token) {
        return powerfulToken.equals(token);
    }

    public void setPowerfulTokenToRedis(UserInfoToken userInfo) {
        if(StrUtil.isEmpty(powerfulToken)) {
            return;
        }
        String keyName = USER_INFO + powerfulToken;
        redisUtil.set(keyName, JSON.toJSONString(userInfo), timeoutSecond);
    }


}
