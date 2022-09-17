package com.xc.usercentor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.usercentor.model.domains.UserTeam;
import com.xc.usercentor.service.UserTeamService;
import com.xc.usercentor.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 25078
* @description 针对表【user_team(用户队伍关联表)】的数据库操作Service实现
* @createDate 2022-09-17 11:43:40
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




