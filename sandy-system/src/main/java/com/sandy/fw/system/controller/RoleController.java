package com.sandy.fw.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sandy.fw.system.models.SysRole;
import com.sandy.fw.system.models.SysRoleMenu;
import com.sandy.fw.system.service.SysRoleMenuService;
import com.sandy.fw.system.service.SysRoleService;
import com.sandy.fw.common.response.ServerResponseEntity;
import com.sandy.fw.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
public class RoleController {

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysRoleMenuService sysRoleMenuService;

    @GetMapping("/list")
    @ApiOperation(value = "获取角色列表")
    public ServerResponseEntity<List<SysRole>> list() {
        List<SysRole> rList = sysRoleService.list();
        return ServerResponseEntity.success(rList);
    }

    @GetMapping("/info/{roleId}")
    @ApiOperation(value = "获取角色信息")
    public ServerResponseEntity<SysRole> info(@PathVariable("roleId") Long roleId) {
        SysRole role = sysRoleService.getById(roleId);
        //查询角色已拥有的对应的菜单及按钮权限
        List<SysRoleMenu> roleMenuList = sysRoleMenuService.list(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, roleId));
        role.setMenuIdList(roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList()));
        return ServerResponseEntity.success(role);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页获取角色列表")
    public ServerResponseEntity<IPage<SysRole>> page(String roleName, Page<SysRole> page) {
        IPage<SysRole> rPage = sysRoleService.page(page, new LambdaQueryWrapper<SysRole>()
                .like(StrUtil.isNotBlank(roleName), SysRole::getName, roleName));
        return ServerResponseEntity.success(rPage);
    }

    @GetMapping("/table")
    @ApiOperation(value = "获取角色列表")
    public ServerResponseEntity<List<SysRole>> table() {
        List<SysRole> rList = sysRoleService.list(new QueryWrapper<SysRole>().lambda().orderByAsc(SysRole::getId));
        return ServerResponseEntity.success(rList);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存角色信息")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public ServerResponseEntity<String> save(@RequestBody SysRole role) {
        role.setCreateBy(SecurityUtils.getUserInfo().getUserId());
        role.setCreateTime(new Date());
        role.setStatus("1");
        sysRoleService.saveRoleAndRoleMenu(role);
        return ServerResponseEntity.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改角色信息")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public ServerResponseEntity<String> update(@RequestBody SysRole role) {
        role.setUpdateBy(SecurityUtils.getUserInfo().getUserId());
        role.setUpdateTime(new Date());
        sysRoleService.updateRoleAndRoleMenu(role);
        return ServerResponseEntity.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除角色信息")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public ServerResponseEntity<String> delete(@RequestBody Long[] roleIds) {
        sysRoleService.deleteRoleByIds(roleIds);
        return ServerResponseEntity.success();
    }
}
