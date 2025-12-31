package com.sandy.fw.pay.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sandy.fw.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付渠道实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pay_channel")
public class PayChannel extends BaseEntity {

    /**
     * 渠道编码
     */
    private String code;

    /**
     * 渠道名称
     */
    private String name;

    /**
     * 渠道配置 JSON
     */
    private String config;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;
}
