package com.sandy.fw.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sandy.fw.admin.models.SysUser;
import com.sandy.fw.admin.service.SysUserService;
import com.sandy.fw.admin.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2024-03-26 16:09:05
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

}




