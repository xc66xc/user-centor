package com.zxl.usercentor.model.request;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 用户登录请求体  用于接收前端的参数
 * @author zxl
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -3675852338894822027L;

    private String userAccount;

    private String userPassword;



}
