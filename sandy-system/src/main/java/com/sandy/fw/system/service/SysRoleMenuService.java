package com.sandy.fw.system.service;

import com.sandy.fw.system.models.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service
* @createDate 2024-03-26 16:08:25
*/
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    void deleteByMenuId(Long menuId);

    void deleteByRoleId(Long[] roleIds);

    void saveRoleAndRoleMenu(List<SysRoleMenu> roleMenuList);
}
