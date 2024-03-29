package com.sandy.fw.admin.controller;

import cn.hutool.core.map.MapUtil;
import com.sandy.fw.admin.models.SysMenu;
import com.sandy.fw.admin.service.SysMenuService;
import com.sandy.fw.common.response.ServerResponseEntity;
import com.sandy.fw.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
@Api(tags = "菜单管理")
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;


    @GetMapping("/nav")
    @ApiOperation(value = "获取用户所拥有的菜单和权限")
    public ServerResponseEntity<Map<Object, Object>> nav() {
        List<SysMenu> menuList = sysMenuService.listMenuByUserId(SecurityUtils.getUserInfo().getUserId());
        return ServerResponseEntity.success(
                MapUtil.builder()
                        .put("menuList", menuList)
                        .put("authorities", SecurityUtils.getUserInfo().getPerms())
                        .build()
        );
    }

}
