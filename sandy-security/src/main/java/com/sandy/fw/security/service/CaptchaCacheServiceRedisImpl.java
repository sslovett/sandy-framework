package com.sandy.fw.security.service;

import com.anji.captcha.service.CaptchaCacheService;
import com.sandy.fw.common.util.RedisUtil;

public class CaptchaCacheServiceRedisImpl implements CaptchaCacheService {

    RedisUtil redisUtil;

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public void set(String s, String s1, long l) {
        redisUtil.set(s, s1, l);
    }

    @Override
    public boolean exists(String s) {
        return redisUtil.hasKey(s);
    }

    @Override
    public void delete(String s) {
        redisUtil.del(s);
    }

    @Override
    public String get(String s) {
        return (String) redisUtil.get(s);
    }

    @Override
    public String type() {
        return "redis";
    }


}
