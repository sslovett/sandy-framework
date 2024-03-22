package com.sandy.fw.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sandy.fw.admin.dto.AuthenticationDTO;
import com.sandy.fw.admin.models.TzSysUser;
import com.sandy.fw.admin.service.TzSysUserService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class LoginController {

    @Autowired
    TzSysUserService tzSysUserService;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ServerResponseEntity<?> login(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
        TzSysUser tzSysUser = tzSysUserService.getOne(new LambdaQueryWrapper<TzSysUser>()
                .eq(TzSysUser::getUsername, authenticationDTO.getUserName()));
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
        userInfo.setUserId(tzSysUser.getUserId().toString());
        userInfo.setUserName(tzSysUser.getUsername());
        userInfo.setStatus(tzSysUser.getStatus()!=0);

        String token = tokenManager.login(userInfo);

        Map<String, String> map = new HashMap<>();
        map.put("token", token);

        return ServerResponseEntity.success(map);
    }
}
