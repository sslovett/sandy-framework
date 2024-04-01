package com.sandy.fw.admin.service;

import com.sandy.fw.admin.models.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2024-03-26 16:09:05
*/
public interface SysUserService extends IService<SysUser> {

    void saveUserAndUserRole(SysUser user);

    void updateUserAndUserRole(SysUser user);

    void deleteUserAndUserRole(Long[] userIds);
}
