package com.sandy.fw.system.models;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户�?
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class SysUser implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户�?
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号状态（0正常 1停用�?
     */
    private Integer status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机�?
     */
    private String phone;

    /**
     * 用户性别�?男，1女，2未知�?
     */
    private String sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型�?代表普通用户，0代表管理�?
     */
    private String userType;

    /**
     * 创建人的用户id
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新�?
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标志�?代表未删除，1代表已删除）
     */
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID列表
     */
    @TableField(exist=false)
    private List<Long> roleIdList;

    /**
     * 角色列表（用于列表展示）
     */
    @TableField(exist=false)
    private List<SysRole> roleList;
}
