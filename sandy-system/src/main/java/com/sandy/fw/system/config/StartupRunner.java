package com.sandy.fw.system.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sandy.fw.system.models.SysMenu;
import com.sandy.fw.system.service.SysMenuService;
import com.sandy.fw.common.util.RedisUtil;
import com.sandy.fw.security.bean.UserInfoToken;
import com.sandy.fw.security.token.TokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StartupRunner {

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    TokenManager tokenManager;

    @PostConstruct
    public void tokenInitializer() {
        log.info("初始化万能token开�?....");
        UserInfoToken userInfo = new UserInfoToken();
        userInfo.setUserId(1L);
        userInfo.setUserName("万能token");
        userInfo.setStatus(true);
        List<SysMenu> menuList = sysMenuService.list(Wrappers.emptyWrapper());
        List<String> permsList = menuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
        Set<String> permsSet = permsList.stream().flatMap((perms) -> {
            if(perms == null || perms.trim().length() == 0) {
                return null;
            }
            return Arrays.stream(perms.split(StrUtil.COMMA));
        }).collect(Collectors.toSet());
        userInfo.setPerms(permsSet);
        tokenManager.setPowerfulTokenToRedis(userInfo);
        log.info("初始化万能token完成.....");
    }
}
