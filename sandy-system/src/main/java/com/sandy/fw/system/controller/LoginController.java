package com.sandy.fw.system.controller;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sandy.fw.system.constant.Constant;
import com.sandy.fw.system.dto.AuthenticationDTO;
import com.sandy.fw.system.dto.CaptchaAuthenticationDTO;
import com.sandy.fw.system.models.SysMenu;
import com.sandy.fw.system.models.SysUser;
import com.sandy.fw.system.service.SysMenuService;
import com.sandy.fw.system.service.SysUserService;
import com.sandy.fw.common.annotation.Log;
import com.sandy.fw.common.exception.DefaultException;
import com.sandy.fw.common.response.ServerResponseEntity;
import com.sandy.fw.security.bean.UserInfoToken;
import com.sandy.fw.security.password.PasswordManager;
import com.sandy.fw.security.token.TokenManager;
import com.sandy.fw.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = "管理员登录")
public class LoginController {

    @Autowired
    SysUserService userService;

    @Autowired
    TokenManager tokenManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    CaptchaService captchaService;

    @Autowired
    PasswordManager passwordManager;


    @PostMapping("/login")
    @ApiOperation(value = "账号密码+验证码登录（用于后台登录）")
    public ServerResponseEntity<?> login(@Valid @RequestBody CaptchaAuthenticationDTO authenticationDTO) {
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(authenticationDTO.getCaptchaVerification());
        ResponseModel responseModel = captchaService.verification(captchaVO);
        if(!responseModel.isSuccess()) {
            return ServerResponseEntity.fail("验证码错误或已过期");
        }

        SysUser tzSysUser = userService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, authenticationDTO.getUserName()));
        if(null == tzSysUser) {
            throw new DefaultException("账号或密码不正确");
        }

        //检查密�?
        passwordManager.check(authenticationDTO.getPassword(), tzSysUser.getPassword());

        if(Objects.equals(tzSysUser.getStatus(), "0")) {
            throw new DefaultException("账号已被禁用");
        }

        UserInfoToken userInfo = new UserInfoToken();
        userInfo.setUserId(tzSysUser.getId());
        userInfo.setUserName(tzSysUser.getUserName());
        userInfo.setStatus(tzSysUser.getStatus() == 1);
        userInfo.setPerms(getUserPermissions(tzSysUser.getId()));//权限列表存入redis，存在的问题是添加或修改权限后需要重新登�?

        String token = tokenManager.login(userInfo);

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", token);

        return ServerResponseEntity.success(map);
    }

    @PostMapping("/logOut")
    @ApiOperation(value = "退出登录")
    @Log("退出登录")
    public ServerResponseEntity<?> logout() {
        //删除用户当前的token信息
        String token = SecurityUtils.getUserInfo().getToken();
        if(StrUtil.isBlank(token)) {
            return ServerResponseEntity.success();
        }
        tokenManager.logout(token);
        return ServerResponseEntity.success();
    }

    private Set<String> getUserPermissions(Long userId) {
        List<String> permsList;
        //超级管理员，具有所有权�?
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
