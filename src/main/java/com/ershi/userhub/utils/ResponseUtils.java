package com.ershi.userhub.utils;

import com.ershi.userhub.common.BaseResponse;
import com.ershi.userhub.common.ErrorCode;

/**
 * 返回工具类
 *
 * @author Eershi
 * @date 2024/04/18
 */
public class ResponseUtils {

    public static <T> BaseResponse<T> success(T data, String description) {
        return new BaseResponse<T>(ErrorCode.SUCCESS, data, description);
    }

    public static BaseResponse error(int code, String Message, String description) {
        return new BaseResponse(code, Message, description);
    }

    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse(errorCode);
    }

    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode, description);
    }
}
