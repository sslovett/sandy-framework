package com.sandy.fw.system.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sandy.fw.system.constant.Constant;
import com.sandy.fw.system.constant.MenuType;
import com.sandy.fw.system.models.SysMenu;
import com.sandy.fw.system.service.SysMenuService;
import com.sandy.fw.common.annotation.Log;
import com.sandy.fw.common.exception.DefaultException;
import com.sandy.fw.common.response.ServerResponseEntity;
import com.sandy.fw.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/menu")
@Api(tags = "菜单管理")
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;


    @GetMapping("/nav")
    @ApiOperation(value = "获取用户所拥有的菜单和权限(不包括按�?")
    public ServerResponseEntity<Map<Object, Object>> nav() {
        List<SysMenu> menuList = sysMenuService.treeMenuNoBtnByUserId(SecurityUtils.getUserInfo().getUserId());
        return ServerResponseEntity.success(
                MapUtil.builder()
                        .put("menuList", menuList)
                        .put("authorities", SecurityUtils.getUserInfo().getPerms())
                        .build()
        );
    }

    @GetMapping("/table")
    @ApiOperation(value = "获取菜单列表(包括按钮)")
    public ServerResponseEntity<List<SysMenu>> table() {
        List<SysMenu> menuList = sysMenuService.listMenuAndBtnByUserId(SecurityUtils.getUserInfo().getUserId());
        return ServerResponseEntity.success(menuList);
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取用户所拥有的菜�?不包括按�?")
    public ServerResponseEntity<List<SysMenu>> list() {
        List<SysMenu> menuList = sysMenuService.listMenuNoBtnByUserId(SecurityUtils.getUserInfo().getUserId());
        return ServerResponseEntity.success(menuList);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增菜单")
    @Log("新增菜单")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public ServerResponseEntity<Void> save(@Valid @RequestBody SysMenu menu) {
        verifyForm(menu);
        sysMenuService.save(menu);
        return ServerResponseEntity.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改菜单")
    @Log("修改菜单")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public ServerResponseEntity<Void> update(@Valid @RequestBody SysMenu menu) {
        verifyForm(menu);
        sysMenuService.updateById(menu);
        return ServerResponseEntity.success();
    }

    @GetMapping("/info/{menuId}")
    @ApiOperation(value = "获取菜单信息")
    @PreAuthorize("hasAuthority('sys:menu:info')")
    public ServerResponseEntity<SysMenu> info(@PathVariable("menuId") Long menuId) {
        SysMenu menu = sysMenuService.getById(menuId);
        return ServerResponseEntity.success(menu);
    }

    @PostMapping("/delete/{menuId}")
    @ApiOperation(value = "删除菜单")
    @Log("删除菜单")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public ServerResponseEntity<Void> delete(@PathVariable("menuId") Long menuId) {
        if(menuId == Constant.SYS_MENU_MAX_ID) {
            return ServerResponseEntity.fail("系统菜单，不能删除");
        }
        //判断是否有子菜单或按�?
        List<SysMenu> menuList = sysMenuService.list(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, menuId));
        if(menuList.size() > 0){
            return ServerResponseEntity.fail("请先删除子菜单或按钮");
        }
        sysMenuService.deleteMenuAndRole(menuId);
        return ServerResponseEntity.success();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenu menu){
        if(menu.getType() == MenuType.MENU.getValue()){
            if(StrUtil.isBlank(menu.getPath())){
                throw new DefaultException("菜单URL不能为空");
            }
        }
        if(Objects.equals(menu.getId(), menu.getParentId())){
            throw new DefaultException("自己不能是自己的上级");
        }

        //上级菜单类型
        int parentType = MenuType.CATALOG.getValue();
        if(menu.getParentId() != 0){
            SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜�?
        if(menu.getType() == MenuType.CATALOG.getValue() ||
                menu.getType() == MenuType.MENU.getValue()){
            if(parentType != MenuType.CATALOG.getValue()){
                throw new DefaultException("上级菜单只能为目录类型");
            }
            return ;
        }

        //按钮
        if(menu.getType() == MenuType.BUTTON.getValue()){
            if(parentType != MenuType.MENU.getValue()){
                throw new DefaultException("上级菜单只能为菜单类型");
            }
        }
    }

}
