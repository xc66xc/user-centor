package com.zxl.usercentor.excption;

import com.zxl.usercentor.common.ErrorCode;

/**
 * 自定义异常类
 *
 * 不用显示去捕获异常，throw 或者 try catch
 * @author zxl
 */
public class BusinessException extends RuntimeException{

    /**
     * final修饰完之后不能提供setter方法了
     */
    private final int code;

    private final String description;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
