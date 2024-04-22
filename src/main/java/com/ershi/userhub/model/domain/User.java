package com.ershi.userhub.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.ershi.userhub.constant.UserConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 用户实体类
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable{

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String userName = UserConstant.DEFAULT_USERNAME;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户头像
     */
    private String avatarUrl = UserConstant.DEFAULT_AVATAR_URL;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phoneNum;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0 - 正常
     */
    private Integer userStatus;

    /**
     * 数据创建时间
     */
    // Mybatis 查询时间采用格林威治时间，比中八区少8小时
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 时区 = 格林威治时间 + 8
    private Date createTime;

    /**
     * 数据更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 该条数据是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     *用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}