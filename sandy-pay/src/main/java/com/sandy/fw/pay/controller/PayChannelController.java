package com.sandy.fw.pay.controller;

import com.sandy.fw.common.response.ServerResponseEntity;
import com.sandy.fw.pay.entity.PayChannel;
import com.sandy.fw.pay.service.PayChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付渠道 Controller
 */
@Api(tags = "支付渠道管理")
@RestController
@RequestMapping("/pay/channel")
public class PayChannelController {

    @Autowired
    private PayChannelService payChannelService;

    @ApiOperation("获取所有支付渠道")
    @GetMapping("/list")
    public ServerResponseEntity<List<PayChannel>> list() {
        return ServerResponseEntity.success(payChannelService.list());
    }

    @ApiOperation("根据ID获取支付渠道")
    @GetMapping("/{id}")
    public ServerResponseEntity<PayChannel> getById(@PathVariable Long id) {
        return ServerResponseEntity.success(payChannelService.getById(id));
    }

    @ApiOperation("根据编码获取支付渠道")
    @GetMapping("/code/{code}")
    public ServerResponseEntity<PayChannel> getByCode(@PathVariable String code) {
        return ServerResponseEntity.success(payChannelService.getByCode(code));
    }
}
