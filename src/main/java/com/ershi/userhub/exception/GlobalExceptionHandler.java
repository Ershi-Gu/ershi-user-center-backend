package com.ershi.userhub.exception;

import com.ershi.userhub.common.BaseResponse;
import com.ershi.userhub.common.ErrorCode;
import com.ershi.userhub.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){

        // 输出日志
        log.error("BusinessException: " + e.getMessage(), e);
        return ResponseUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }


    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){

        // 输出日志
        log.error("RuntimeException", e);
        return ResponseUtils.error(ErrorCode.SYSTEM_ERROR, "服务器异常");
    }
}
