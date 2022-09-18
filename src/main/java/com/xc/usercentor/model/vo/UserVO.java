package com.xc.usercentor.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * @TableName user
 */

@Data
public class UserVO implements Serializable {
    /**
     * id
     */
    private long id;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 标签列表tags
     */
    private String tags;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 性别
     */
    private Integer gender;


    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;


    /**
     * 平台专属编号
     */
    private String planetCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态 
     */
    private Integer userStatus;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 用户角色 0-普通用户  1-管理员
     */
    private Integer userRole;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}