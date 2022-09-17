package com.xc.usercentor.common;

/**
 * 返回工具类
 *
 * @author zxl
 */
public class ResultUtils {

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }


    /**
     * 失败
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> fail(T data) {
        return new BaseResponse<>(1, data, "bad");
    }

    /**
     * 错误
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse(errorCode);
    }

    /**
     * 错误
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String message, String description) {
        return new BaseResponse(errorCode.getCode(), null, message, description);
    }

    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), null,errorCode.getMessage(), description);
    }

    public static BaseResponse error(int code, String message,String description) {
        return new BaseResponse(code, null, message, description);
    }
}
