package com.sandy.fw.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sandy.fw.system.mapper.SysUserRoleMapper;
import com.sandy.fw.system.models.SysUser;
import com.sandy.fw.system.models.SysUserRole;
import com.sandy.fw.system.service.SysUserRoleService;
import com.sandy.fw.system.service.SysUserService;
import com.sandy.fw.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_user(用户�?】的数据库操作Service实现
* @createDate 2024-03-26 16:09:05
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Autowired
    SysUserRoleService userRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserAndUserRole(SysUser user) {
        //保存用户信息
        this.save(user);
        if(CollUtil.isEmpty(user.getRoleIdList())) {
            return ;
        }
        //保存用户与角色的关系
        userRoleService.saveBatch(user.getId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAndUserRole(SysUser user) {
        this.updateById(user);
        if(CollUtil.isEmpty(user.getRoleIdList())) {
            return ;
        }
        //删除用户与角色关�?
        userRoleService.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, user.getId()));
        //保存用户与角色关�?
        userRoleService.saveBatch(user.getId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserAndUserRole(Long[] userIds) {
        //删除用户信息
        this.removeByIds(CollUtil.toList(userIds));
        //删除用户与角色关�?
        userRoleService.remove(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, userIds));
    }
}




