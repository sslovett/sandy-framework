package com.sandy.fw.system.mapper;

import com.sandy.fw.system.models.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_menu(菜单权限�?】的数据库操作Mapper
* @createDate 2024-03-25 15:55:16
* @Entity com.sandy.fw.admin.models.SysMenu
*/
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<String> getPermsByUserId(Long userId);

    List<SysMenu> listMenuByUserId(Long userId);

    List<SysMenu> listMenuNoBtnByUserId(Long userId);
}




