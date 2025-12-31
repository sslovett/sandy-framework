package com.sandy.fw.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sandy.fw.system.constant.Constant;
import com.sandy.fw.system.mapper.SysRoleMenuMapper;
import com.sandy.fw.system.models.SysMenu;
import com.sandy.fw.system.service.SysMenuService;
import com.sandy.fw.system.mapper.SysMenuMapper;
import com.sandy.fw.system.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
* @author Administrator
* @description 针对表【sys_menu(菜单权限�?】的数据库操作Service实现
* @createDate 2024-03-25 15:55:16
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{

    @Autowired
    SysMenuMapper sysMenuMapper;

    @Autowired
    SysRoleMenuService sysRoleMenuService;

    @Override
    public List<String> getUserPermissions(Long userId) {
        return sysMenuMapper.getPermsByUserId(userId);
    }

    @Override
    public List<SysMenu> treeMenuNoBtnByUserId(Long userId) {
        List<SysMenu> sysMenus;
        //超级管理员，拥有最高权�?
        if(userId == Constant.SUPER_ADMIN_ID) {
            sysMenus = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .ne(SysMenu::getType, 2)
                    .orderByAsc(SysMenu::getOrderNum));
        } else {
            sysMenus = sysMenuMapper.listMenuNoBtnByUserId(userId);
        }
        List<SysMenu> rootMenus = new ArrayList<>();
        for(SysMenu sysMenu : sysMenus) {
            if(sysMenu.getParentId() == 0) {
                rootMenus.add(sysMenu);
            }
            for(SysMenu rootMenu : rootMenus) {
                if(sysMenu.getParentId().equals(rootMenu.getId())) {
                    if(rootMenu.getList() == null) {
                        rootMenu.setList(new ArrayList<>());
                    }
                    rootMenu.getList().add(sysMenu);
                }
            }
        }
        return rootMenus;
    }

    @Override
    public List<SysMenu> listMenuAndBtnByUserId(Long userId) {
        List<SysMenu> sysMenus;
        //超级管理员，拥有最高权�?
        if(userId == Constant.SUPER_ADMIN_ID) {
            sysMenus = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .orderByAsc(SysMenu::getOrderNum));
        } else {
            sysMenus = sysMenuMapper.listMenuByUserId(userId);
        }
        return sysMenus;
    }

    @Override
    public List<SysMenu> listMenuNoBtnByUserId(Long userId) {
        List<SysMenu> sysMenus;
        //超级管理员，拥有最高权�?
        if(userId == Constant.SUPER_ADMIN_ID) {
            sysMenus = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .ne(SysMenu::getType, 2)
                    .orderByAsc(SysMenu::getOrderNum));
        } else {
            sysMenus = sysMenuMapper.listMenuNoBtnByUserId(userId);
        }
        return sysMenus;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenuAndRole(Long menuId) {
        this.removeById(menuId);
        //删除角色关联关系
        sysRoleMenuService.deleteByMenuId(menuId);
    }
}




