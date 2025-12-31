package com.sandy.fw.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sandy.fw.system.models.SysRole;
import com.sandy.fw.system.models.SysRoleMenu;
import com.sandy.fw.system.service.SysRoleMenuService;
import com.sandy.fw.system.service.SysRoleService;
import com.sandy.fw.system.mapper.SysRoleMapper;
import com.sandy.fw.system.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_role(角色信息�?】的数据库操作Service实现
* @createDate 2024-03-26 16:09:00
*/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{

    @Autowired
    SysRoleMenuService sysRoleMenuService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleAndRoleMenu(SysRole role) {
        this.save(role);
        saveRoleMenu(role.getMenuIdList(), role.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleAndRoleMenu(SysRole role) {
        this.updateById(role);
        //删除角色与菜单关�?
        sysRoleMenuService.deleteByRoleId(new Long[]{role.getId()});
        //保存角色与菜单关�?
        saveRoleMenu(role.getMenuIdList(), role.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleByIds(Long[] roleIds) {
        //删除角色
        this.removeBatchByIds(CollUtil.toList(roleIds));
        //删除角色与菜单关�?
        sysRoleMenuService.deleteByRoleId(roleIds);
        //删除角色与用户的关联
        sysUserRoleService.deleteByRoleId(roleIds);
    }

    private void saveRoleMenu(List<Long> menuIdList, long roleId) {
        if(menuIdList.size() == 0) {
            return ;
        }

        List<SysRoleMenu> roleMenuList = new ArrayList<>();
        menuIdList.forEach(menuId -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            roleMenuList.add(sysRoleMenu);
        });

        sysRoleMenuService.saveRoleAndRoleMenu(roleMenuList);
    }
}




