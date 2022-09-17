package com.xc.usercentor.service;

import com.xc.usercentor.model.domains.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.usercentor.model.domains.User;

/**
* @author 25078
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2022-09-17 11:41:33
*/
public interface TeamService extends IService<Team> {
    /**
     * 创建队伍
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);
}
