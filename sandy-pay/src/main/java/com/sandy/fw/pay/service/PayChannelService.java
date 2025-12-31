package com.sandy.fw.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sandy.fw.pay.entity.PayChannel;

/**
 * 支付渠道 Service 接口
 */
public interface PayChannelService extends IService<PayChannel> {

    /**
     * 根据渠道编码获取渠道
     * @param code 渠道编码
     * @return 支付渠道
     */
    PayChannel getByCode(String code);
}
