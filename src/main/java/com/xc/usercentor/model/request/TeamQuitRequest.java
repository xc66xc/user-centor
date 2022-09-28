package com.xc.usercentor.model.request;


import lombok.Data;

import java.io.Serializable;

@Data
public class TeamQuitRequest implements Serializable {


    private static final long serialVersionUID = -2674967894947143030L;
    /**
     * id
     */
    private Long teamId;


}
