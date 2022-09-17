package com.xc.usercentor.common;

/**
 * 错误码
 *
 * @author zxl
 */

public enum ErrorCode {

    NO_AUTH(40100, "无权限", ""),
    NO_LOGIN(40200, "未登录", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    PARAMS_NULL_ERROR(40001, "请求参数为空", ""),
    NULL_ERROR(40003, "参数为空", ""),
    SUCCESS(0, "OK", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码详情
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
