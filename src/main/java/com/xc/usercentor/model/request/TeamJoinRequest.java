package com.xc.usercentor.model.request;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TeamJoinRequest implements Serializable {


    private static final long serialVersionUID = -2674967894947143030L;
    /**
     * id
     */
    private Long teamId;


    /**
     * 密码
     */
    private String password;

}
