package com.sandy.fw.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sandy.fw.admin.models.SysMenu;
import com.sandy.fw.admin.service.SysMenuService;
import com.sandy.fw.admin.mapper.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
* @author Administrator
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2024-03-25 15:55:16
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{

    @Autowired
    SysMenuMapper sysMenuMapper;

    @Override
    public List<String> getUserPermissions(Long userId) {
        return sysMenuMapper.getPermsByUserId(userId);
    }
}




