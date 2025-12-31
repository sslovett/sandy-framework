package com.sandy.fw.system.service;

import com.sandy.fw.system.models.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service
* @createDate 2024-03-26 16:32:38
*/
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 批量保存用户和角色关�?
     * @param userId
     * @param roleIdList
     */
    void saveBatch(Long userId, List<Long> roleIdList);

    void deleteByRoleId(Long[] roleIds);
}
