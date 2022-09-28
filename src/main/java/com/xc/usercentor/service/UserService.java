package com.xc.usercentor.service;

import com.xc.usercentor.common.BaseResponse;
import com.xc.usercentor.model.domains.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.usercentor.model.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.xc.usercentor.constant.UserConstant.ADMIN_ROLE;
import static com.xc.usercentor.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author zxl
 * @description 用户服务
 * @createDate 2022-04-01 23:27:42
 */
@Service
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount 账号
     * @param userPassword 密码
     * @param checkPassword 校验码
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 返回脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * search users by tags
     * @param tagLists
     * @return
     */
    List<User> searchUsersByTags(List<String> tagLists);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    Integer updateUser(User user,User loginUser);

    /**
     * 获取当前登录用户信息
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 是否为管理员  是-true
     *
     * @param servletRequest
     * @return
     */
    boolean isAdmin(HttpServletRequest servletRequest);

    boolean isAdmin(User loginUser);

    /**
     * 匹配用户
     * @param num
     * @param loginUser
     * @return
     */
    List<User> matchUsers(long num, User loginUser);
}
