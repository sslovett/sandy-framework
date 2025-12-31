package com.sandy.fw.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sandy.fw.system.models.SysLog;
import com.sandy.fw.system.service.SysLogService;
import com.sandy.fw.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log")
@Api(tags = "操作日志管理")
public class LogController {

    @Autowired
    SysLogService sysLogService;

    @GetMapping("/page")
    @ApiOperation(value = "分页获取操作日志列表")
    public ServerResponseEntity<IPage<SysLog>> page(SysLog sysLog, Page<SysLog> page) {
        IPage<SysLog> sysLogs = sysLogService.page(page, new LambdaQueryWrapper<SysLog>()
                .like(StrUtil.isNotBlank(sysLog.getUsername()), SysLog::getUsername, sysLog.getUsername())
                .like(StrUtil.isNotBlank(sysLog.getOperation()), SysLog::getOperation, sysLog.getOperation())
                .orderByDesc(SysLog::getCreateDate)
        );
        return ServerResponseEntity.success(sysLogs);
    }
}
