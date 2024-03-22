package com.sandy.fw.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sandy.fw.admin.models.User;
import com.sandy.fw.admin.service.UserService;
import com.sandy.fw.common.exception.DefaultException;
import com.sandy.fw.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ServerResponseEntity<List<User>> list(User user) {
        List<User> areaList = userService.list(new LambdaQueryWrapper<User>()
                .like(user.getName() != null, User::getName, user.getName()));
        return ServerResponseEntity.success(areaList);
    }

    @GetMapping("/page")
    public ServerResponseEntity<IPage<User>> page(Page<User> page) {
        IPage<User> areaList = userService.page(page, new LambdaQueryWrapper<User>());
        return ServerResponseEntity.success(areaList);
    }

    @GetMapping("/info/{id}")
    public ServerResponseEntity<User> info(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return ServerResponseEntity.success(user);
    }

    @PostMapping("/save")
    public ServerResponseEntity<Void> save(@Valid @RequestBody User user){
        userService.save(user);
        return ServerResponseEntity.success();
    }

    @GetMapping("/testException")
    public void testException() {
        throw new DefaultException("测试异常");
    }



}
