package com.sandy.fw.admin.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tz_area
 */
@TableName(value ="tz_area")
@Data
public class TzArea implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long areaId;

    /**
     * 
     */
    private String areaName;

    /**
     * 
     */
    private Long parentId;

    /**
     * 
     */
    private Integer level;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}