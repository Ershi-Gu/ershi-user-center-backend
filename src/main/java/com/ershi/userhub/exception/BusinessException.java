package com.ershi.userhub.exception;

import com.ershi.userhub.common.ErrorCode;
import lombok.Getter;

/**
 * 业务异常类 - 继承 RuntimeException, 扩充额外字段 code - 状态码, description - 状态详细描述。
 * @author Eershi
 * @date 2024/04/18
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 异常状态码
     */
    private final int code;

    /**
     * 状态详细信息
     */
    private final String description;



    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode){
        // message 设置
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description){
        // message 设置
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }
}
