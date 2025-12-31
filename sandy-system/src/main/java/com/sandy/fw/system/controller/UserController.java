package com.sandy.fw.system.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sandy.fw.system.constant.Constant;
import com.sandy.fw.system.dto.UpdatePasswordDTO;
import com.sandy.fw.system.models.SysRole;
import com.sandy.fw.system.models.SysUser;
import com.sandy.fw.system.models.SysUserRole;
import com.sandy.fw.system.service.SysRoleService;
import com.sandy.fw.system.service.SysUserRoleService;
import com.sandy.fw.system.service.SysUserService;
import com.sandy.fw.common.annotation.Log;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Api(tags = "管理员信息")
public class UserController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private PasswordManager passwordManager;

    @Autowired
    private TokenManager tokenManager;


    @Autowired
    private SysRoleService roleService;

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('sys:user:page')")
    public ServerResponseEntity<IPage<SysUser>> page(String userName, Page<SysUser> page) {
        IPage<SysUser> userPage = userService.page(page, new LambdaQueryWrapper<SysUser>()
                .like(StrUtil.isNotBlank(userName), SysUser::getUserName, userName));
        
        // 查询每个用户的角色列�?
        for (SysUser user : userPage.getRecords()) {
            List<SysUserRole> userRoleList = userRoleService.list(new LambdaQueryWrapper<SysUserRole>()
                    .eq(SysUserRole::getUserId, user.getId()));
            if (!userRoleList.isEmpty()) {
                List<Long> roleIds = userRoleList.stream()
                        .map(SysUserRole::getRoleId)
                        .collect(Collectors.toList());
                List<SysRole> roles = roleService.listByIds(roleIds);
                user.setRoleList(roles);
                user.setRoleIdList(roleIds);
            }
        }
        
        return ServerResponseEntity.success(userPage);
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取登录用户信息")
    public ServerResponseEntity<SysUser> info() {
        SysUser user = userService.getById(SecurityUtils.getUserInfo().getUserId());
        return ServerResponseEntity.success(user);
    }

    @GetMapping("/info/{userId}")
    @PreAuthorize("hasAuthority('sys:user:info')")
    @ApiOperation(value = "根据id获取用户信息")
    public ServerResponseEntity<SysUser> info(@PathVariable("userId") Long userId) {
        SysUser user = userService.getById(userId);
        List<SysUserRole> userRoleList = userRoleService.list(new LambdaQueryWrapper<>(SysUserRole.class)
                .eq(SysUserRole::getUserId, userId));
        user.setRoleIdList(userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList()));
        return ServerResponseEntity.success(user);
    }

    @PostMapping("/password")
    @ApiOperation(value = "修改密码")
    @Log("修改密码")
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

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    @ApiOperation(value = "新增管理员")
    @Log("新增管理员")
    public ServerResponseEntity<Void> save(@Valid @RequestBody SysUser user){
        SysUser dbUser = userService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, user.getUserName()));
        if (null != dbUser) {
            throw new DefaultException("用户名已存在");
        }
        user.setPassword(passwordManager.encode(user.getPassword()));
        user.setCreateBy(SecurityUtils.getUserInfo().getUserId());
        user.setCreateTime(new Date());
        userService.saveUserAndUserRole(user);
        return ServerResponseEntity.success();
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    @ApiOperation(value = "修改管理员")
    @Log("修改管理员")
    public ServerResponseEntity<Void> update(@Valid @RequestBody SysUser user){
        //禁止修改管理员信�?
        checkAdmin(user);
        //判断账号是否存在
        SysUser dbUser = userService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, user.getUserName()));
        if (null != dbUser && !dbUser.getId().equals(user.getId())) {
            throw new DefaultException("用户名已存在");
        }
        //判断是否需要修改密�?
        if (StrUtil.isNotBlank(user.getPassword())) {
            user.setPassword(passwordManager.encode(user.getPassword()));
        }else {
            user.setPassword(null);
        }
        user.setUpdateBy(SecurityUtils.getUserInfo().getUserId());
        user.setUpdateTime(new Date());
        userService.updateUserAndUserRole(user);
        return ServerResponseEntity.success();
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @ApiOperation(value = "删除管理员")
    @Log("删除管理员")
    public ServerResponseEntity<Void> delete(@RequestBody Long[] userIds){
        if(userIds.length == 0) {
            return ServerResponseEntity.fail("请选择要删除的用户");
        }
        if(ArrayUtil.contains(userIds, SecurityUtils.getUserInfo().getUserId())) {
            return ServerResponseEntity.fail("禁止删除当前登录用户");
        }
        //禁止删除超级管理员账�?
        for(Long userId : userIds) {
            checkAdmin(userId);
        }
        userService.deleteUserAndUserRole(userIds);
        return ServerResponseEntity.success();
    }

    /**
     * 禁止修改管理员账号信息
     */
    private void checkAdmin(SysUser user) {
        if (user.getId() == Constant.SUPER_ADMIN_ID || user.getUserName().equals(Constant.SUPER_ADMIN_NAME)) {
            throw new DefaultException("禁止修改管理员账号信息");
        }
    }

    private void checkAdmin(Long userId) {
        if (userId == Constant.SUPER_ADMIN_ID) {
            throw new DefaultException("禁止修改管理员账号信息");
        }
    }

}
