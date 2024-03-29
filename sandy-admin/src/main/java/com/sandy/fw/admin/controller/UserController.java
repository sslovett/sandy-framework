package com.sandy.fw.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sandy.fw.admin.dto.UpdatePasswordDTO;
import com.sandy.fw.admin.models.SysUser;
import com.sandy.fw.admin.service.SysUserService;
import com.sandy.fw.common.exception.DefaultException;
import com.sandy.fw.common.response.ServerResponseEntity;
import com.sandy.fw.security.password.PasswordManager;
import com.sandy.fw.security.token.TokenManager;
import com.sandy.fw.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private PasswordManager passwordManager;

    @Autowired
    private TokenManager tokenManager;

//    @GetMapping("/list")
//    @PreAuthorize("hasAuthority('sys:user:list')")
//    public ServerResponseEntity<List<SysUser>> list(SysUser user) {
//        List<SysUser> areaList = userService.list(new LambdaQueryWrapper<SysUser>()
//                .like(user.getUserName() != null, SysUser::getUserName, user.getUserName()));
//        return ServerResponseEntity.success(areaList);
//    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('sys:user:page')")
    public ServerResponseEntity<IPage<SysUser>> page(String userName, Page<SysUser> page) {
        IPage<SysUser> areaList = userService.page(page, new LambdaQueryWrapper<SysUser>()
                .like(StrUtil.isNotBlank(userName), SysUser::getUserName, userName));
        return ServerResponseEntity.success(areaList);
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取登录用户信息")
    public ServerResponseEntity<SysUser> info() {
        SysUser user = userService.getById(SecurityUtils.getUserInfo().getUserId());
        return ServerResponseEntity.success(user);
    }

    @PostMapping("/password")
    @ApiOperation(value = "修改密码")
    public ServerResponseEntity<Void> password(@Valid @RequestBody UpdatePasswordDTO param) {
        SysUser sysUser = userService.getById(SecurityUtils.getUserInfo().getUserId());
        if (null == sysUser) {
            throw new DefaultException("原密码错误");
        }
        passwordManager.check(param.getPassword(), sysUser.getPassword());
        sysUser.setPassword(passwordManager.encode(param.getNewPassword()));
        //更新密码
        userService.updateById(sysUser);
        //删除缓存信息
        tokenManager.logout(SecurityUtils.getUserInfo().getToken());

        return ServerResponseEntity.success();
    }

//    @PostMapping("/save")
//    public ServerResponseEntity<Void> save(@Valid @RequestBody SysUser user){
//        userService.save(user);
//        return ServerResponseEntity.success();
//    }



}
