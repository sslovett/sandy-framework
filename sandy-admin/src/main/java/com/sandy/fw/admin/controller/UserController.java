package com.sandy.fw.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sandy.fw.admin.models.SysUser;
import com.sandy.fw.admin.service.SysUserService;
import com.sandy.fw.common.exception.DefaultException;
import com.sandy.fw.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/info")
public class UserController {

    @Autowired
    private SysUserService userService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public ServerResponseEntity<List<SysUser>> list(SysUser user) {
        List<SysUser> areaList = userService.list(new LambdaQueryWrapper<SysUser>()
                .like(user.getUserName() != null, SysUser::getUserName, user.getUserName()));
        return ServerResponseEntity.success(areaList);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('sys:user:page')")
    public ServerResponseEntity<IPage<SysUser>> page(Page<SysUser> page) {
        IPage<SysUser> areaList = userService.page(page, new LambdaQueryWrapper<SysUser>());
        return ServerResponseEntity.success(areaList);
    }

    @GetMapping("/{id}")
    public ServerResponseEntity<SysUser> info(@PathVariable("id") Long id) {
        SysUser user = userService.getById(id);
        return ServerResponseEntity.success(user);
    }

    @PostMapping("/save")
    public ServerResponseEntity<Void> save(@Valid @RequestBody SysUser user){
        userService.save(user);
        return ServerResponseEntity.success();
    }

    @GetMapping("/testException")
    public void testException() {
        throw new DefaultException("测试异常");
    }



}
