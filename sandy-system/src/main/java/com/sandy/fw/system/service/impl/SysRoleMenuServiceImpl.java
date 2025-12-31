package com.sandy.fw.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sandy.fw.system.models.SysRoleMenu;
import com.sandy.fw.system.service.SysRoleMenuService;
import com.sandy.fw.system.mapper.SysRoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2024-03-26 16:08:25
*/
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
    implements SysRoleMenuService{

    @Autowired
    SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public void deleteByMenuId(Long menuId) {
        remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, menuId));
    }

    @Override
    public void deleteByRoleId(Long[] roleIds) {
        sysRoleMenuMapper.deleteByRoleId(roleIds);
    }

    @Override
    public void saveRoleAndRoleMenu(List<SysRoleMenu> roleMenuList) {
        this.saveBatch(roleMenuList);
    }
}




