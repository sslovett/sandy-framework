package com.sandy.fw.system.mapper;

import com.sandy.fw.system.models.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Mapper
* @createDate 2024-03-26 16:32:38
* @Entity com.sandy.fw.admin.models.SysUserRole
*/
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    void deleteByRoleId(Long[] roleIds);
}




