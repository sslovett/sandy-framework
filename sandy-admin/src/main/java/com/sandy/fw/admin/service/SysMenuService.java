package com.sandy.fw.admin.service;

import com.sandy.fw.admin.models.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
* @author Administrator
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2024-03-25 15:55:16
*/
public interface SysMenuService extends IService<SysMenu> {

    List<String> getUserPermissions(Long userId);

    List<SysMenu> listMenuByUserId(Long userId);
}
