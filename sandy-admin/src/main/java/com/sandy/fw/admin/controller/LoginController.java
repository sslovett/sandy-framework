package com.sandy.fw.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sandy.fw.admin.constant.Constant;
import com.sandy.fw.admin.dto.AuthenticationDTO;
import com.sandy.fw.admin.models.SysMenu;
import com.sandy.fw.admin.models.SysUser;
import com.sandy.fw.admin.service.SysMenuService;
import com.sandy.fw.admin.service.SysUserService;
import com.sandy.fw.common.exception.DefaultException;
import com.sandy.fw.common.response.ServerResponseEntity;
import com.sandy.fw.security.bean.UserInfoToken;
import com.sandy.fw.security.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.sql.Wrapper;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class LoginController {

    @Autowired
    SysUserService userService;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SysMenuService sysMenuService;

    @PostMapping("/login")
    public ServerResponseEntity<?> login(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
        SysUser tzSysUser = userService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, authenticationDTO.getUserName()));
        if(null == tzSysUser) {
            throw new DefaultException("账号或密码不正确");
        }

        if(!passwordEncoder.matches(authenticationDTO.getPassword(), tzSysUser.getPassword())) {
            throw new DefaultException("账号或密码不正确");
        }

        if(Objects.equals(tzSysUser.getStatus(), 0)) {
            throw new DefaultException("账号已被禁用");
        }

        UserInfoToken userInfo = new UserInfoToken();
        userInfo.setUserId(tzSysUser.getId().toString());
        userInfo.setUserName(tzSysUser.getUserName());
        userInfo.setStatus(tzSysUser.getStatus().equals("1"));
        userInfo.setPerms(getUserPermissions(tzSysUser.getId()));

        String token = tokenManager.login(userInfo);

        Map<String, String> map = new HashMap<>();
        map.put("token", token);

        return ServerResponseEntity.success(map);
    }

    private Set<String> getUserPermissions(Long userId) {
        List<String> permsList;
        //超级管理员，具有所有权限
        if(userId == Constant.SUPER_ADMIN_ID) {
            List<SysMenu> menuList = sysMenuService.list(Wrappers.emptyWrapper());
            permsList = menuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
        }else {
            permsList = sysMenuService.getUserPermissions(userId);
        }
        return permsList.stream().flatMap((perms) -> {
            if(perms == null || perms.trim().length() == 0) {
                return null;
            }
            return Arrays.stream(perms.split(StrUtil.COMMA));
        }).collect(Collectors.toSet());
    }
}
