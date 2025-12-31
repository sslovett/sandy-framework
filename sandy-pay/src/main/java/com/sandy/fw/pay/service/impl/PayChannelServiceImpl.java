package com.sandy.fw.pay.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sandy.fw.pay.entity.PayChannel;
import com.sandy.fw.pay.mapper.PayChannelMapper;
import com.sandy.fw.pay.service.PayChannelService;
import org.springframework.stereotype.Service;

/**
 * 支付渠道 Service 实现类
 * 使用 @DS("pay") 注解指定使用 pay 数据源
 */
@DS("pay")
@Service
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannel> implements PayChannelService {

    @Override
    public PayChannel getByCode(String code) {
        LambdaQueryWrapper<PayChannel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayChannel::getCode, code);
        return getOne(wrapper);
    }
}
