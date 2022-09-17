package com.xc.usercentor.excption;

import com.xc.usercentor.common.BaseResponse;
import com.xc.usercentor.common.ErrorCode;
import com.xc.usercentor.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 利用SpringAop: 方法前后做一些额外的处理
 *
 * @author zxl
 */
@RestControllerAdvice
@Slf4j
public class GlobelExceptionHandler {
    /**
     * 此方法只去捕获BusinessException异常
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());

    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
