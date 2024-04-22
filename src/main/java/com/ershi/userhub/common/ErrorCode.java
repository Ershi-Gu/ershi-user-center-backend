package com.ershi.userhub.common;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS(0, "success", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NO_LOGIN(40101, "未登录", ""),
    NO_AUTH(40100, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");


    /**
     * 错误状态码
     */
    private final Integer code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述 (详情)
     */
    private final String description;

    ErrorCode(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
    }
