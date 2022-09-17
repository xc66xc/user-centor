package com.xc.usercentor.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * vo 值对象，一般返回给页面用的
 * 用户注册请求体
 *
 * @author zxl
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -445039254859902918L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;

}
