package com.sandy.fw.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sandy.fw.pay.entity.PayChannel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付渠道 Mapper 接口
 */
@Mapper
public interface PayChannelMapper extends BaseMapper<PayChannel> {
}
