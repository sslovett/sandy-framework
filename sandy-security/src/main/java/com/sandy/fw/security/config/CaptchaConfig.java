package com.sandy.fw.security.config;

import com.anji.captcha.model.common.CaptchaTypeEnum;
import com.anji.captcha.model.common.Const;
import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import com.anji.captcha.util.ImageUtils;
import com.sandy.fw.common.util.RedisUtil;
import com.sandy.fw.security.service.CaptchaCacheServiceRedisImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class CaptchaConfig {

    @Bean
    public CaptchaService captchaService() {
        Properties config = new Properties();
        config.put(Const.CAPTCHA_CACHETYPE, "redis");
        config.put(Const.CAPTCHA_WATER_MARK, "");
        // 滑动验证
        config.put(Const.CAPTCHA_TYPE, CaptchaTypeEnum.BLOCKPUZZLE.getCodeValue());
        config.put(Const.CAPTCHA_INIT_ORIGINAL, "true");
        initializeBaseMap();
        return CaptchaServiceFactory.getInstance(config);
    }

    @Bean
    public CaptchaCacheService captchaCacheService(RedisUtil redisUtil) {
        CaptchaCacheService ret = CaptchaServiceFactory.getCache("redis");
        if (ret instanceof CaptchaCacheServiceRedisImpl) {
            ((CaptchaCacheServiceRedisImpl) ret).setRedisUtil(redisUtil);
        }
        return ret;
    }

    private static void initializeBaseMap() {
        ImageUtils.cacheBootImage(getResourcesImagesFile("classpath:captcha" + "/original/*.png"),
                getResourcesImagesFile("classpath:captcha" + "/slidingBlock/*.png"),
                Collections.emptyMap());
    }

    public static Map<String, String> getResourcesImagesFile(String path) {
        Map<String, String> imgMap = new HashMap<>(16);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            Resource[] resources = resolver.getResources(path);
            Resource[] var4 = resources;
            int var5 = resources.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Resource resource = var4[var6];
                byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
                String string = Base64Utils.encodeToString(bytes);
                String filename = resource.getFilename();
                imgMap.put(filename, string);
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        return imgMap;
    }
}
