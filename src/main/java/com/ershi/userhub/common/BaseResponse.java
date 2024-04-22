package com.ershi.userhub.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回对象 <br>
 * code: 业务状态码<br>
 * data: 数据<br>
 * message: 详细执行信息<br>
 *
 * @author Eershi
 * @date 2024/04/18
 */
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 业务状态码 <br>
     * 1. 成功 - 0 <br>
     */
    private Integer code;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 状态码信息
     */
    private String message;

    /**
     * 详细状态信息
     */
    private String description;


    public BaseResponse(Integer code, String message, String description) {
        this.code = code;
        this.data = null;
        this.message = message;
        this.description = description;
    }


    /**
     * 带数据的返回对象
     *
     * @param code
     * @param data
     * @param message
     * @param description
     */
    public BaseResponse(Integer code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    /**
     * @param errorCode
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }


    public BaseResponse(ErrorCode errorCode, String description) {
        this(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    public BaseResponse(ErrorCode errorCode, T data, String description) {
        this(errorCode.getCode(), data, errorCode.getMessage(), description);
    }
}
