package com.sandy.fw.system.service;

import com.sandy.fw.system.models.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【sys_role(角色信息�?】的数据库操作Service
* @createDate 2024-03-26 16:09:00
*/
public interface SysRoleService extends IService<SysRole> {

    void saveRoleAndRoleMenu(SysRole role);

    void updateRoleAndRoleMenu(SysRole role);

    void deleteRoleByIds(Long[] roleId);
}
