package com.sandy.fw.system.service;

import com.sandy.fw.system.models.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
* @author Administrator
* @description 针对表【sys_menu(菜单权限�?】的数据库操作Service
* @createDate 2024-03-25 15:55:16
*/
public interface SysMenuService extends IService<SysMenu> {

    List<String> getUserPermissions(Long userId);

    List<SysMenu> treeMenuNoBtnByUserId(Long userId);

    List<SysMenu> listMenuAndBtnByUserId(Long userId);

    List<SysMenu> listMenuNoBtnByUserId(Long userId);

    void deleteMenuAndRole(Long menuId);
}
