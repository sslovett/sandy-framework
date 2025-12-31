package com.sandy.fw.system.mapper;

import com.sandy.fw.system.models.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Mapper
* @createDate 2024-03-26 16:08:25
* @Entity com.sandy.fw.admin.models.SysRoleMenu
*/
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    void deleteByRoleId(Long[] roleIds);
}




