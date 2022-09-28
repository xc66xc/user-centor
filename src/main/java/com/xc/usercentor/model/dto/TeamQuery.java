package com.xc.usercentor.model.dto;


import com.xc.usercentor.model.request.PageRequest;
import lombok.Data;

import java.util.List;


@Data
public class TeamQuery extends PageRequest {
    /**
     * id
     */
    private Long id;

    /**
     * id 列表
     */
    private List<Long> idList;
    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;


}
