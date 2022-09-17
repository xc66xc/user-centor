package com.xc.usercentor.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回结果集
 *
 * @author zxl
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        //构造函数调用其他的构造函数
        this(code, data, "","");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null,errorCode.getMessage(),errorCode.getDescription());
    }


    private int code;
    private T data;
    private String message;
    private String description;
}
