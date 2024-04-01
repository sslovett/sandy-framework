package com.sandy.fw.admin.controller;

import com.sandy.fw.admin.models.SysRole;
import com.sandy.fw.admin.service.SysRoleService;
import com.sandy.fw.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
public class RoleController {

    @Autowired
    SysRoleService sysRoleService;

    @GetMapping("/list")
    @ApiOperation(value = "获取角色列表")
    public ServerResponseEntity<List<SysRole>> list() {
        List<SysRole> rList = sysRoleService.list();
        return ServerResponseEntity.success(rList);
    }
}
