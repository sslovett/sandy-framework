package com.sandy.fw.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sandy.fw.system.models.SysUserRole;
import com.sandy.fw.system.service.SysUserRoleService;
import com.sandy.fw.system.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2024-03-26 16:32:38
*/
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
    implements SysUserRoleService{

    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

    @Override
    public void saveBatch(Long userId, List<Long> roleIdList) {
        List<SysUserRole> userRoles = new ArrayList<>();
        roleIdList.forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        });
        this.saveBatch(userRoles);
    }

    @Override
    public void deleteByRoleId(Long[] roleIds) {
        sysUserRoleMapper.deleteByRoleId(roleIds);
    }
}




