package com.xc.usercentor.service;

import com.xc.usercentor.model.domains.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.usercentor.model.domains.User;
import com.xc.usercentor.model.dto.TeamQuery;
import com.xc.usercentor.model.request.TeamJoinRequest;
import com.xc.usercentor.model.request.TeamQuitRequest;
import com.xc.usercentor.model.request.TeamUpdateRequest;
import com.xc.usercentor.model.vo.TeamUserVO;

import java.util.List;

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

    /**
     * 搜索队伍
     * @param teamQuery
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery,boolean isAdmin);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 解散队伍
     * @param id
     * @return
     */
    boolean deleteTeam(long id, User loginUser);
}
