package com.xc.usercentor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xc.usercentor.common.ErrorCode;
import com.xc.usercentor.mapper.UserMapper;
import com.xc.usercentor.model.domains.User;
import com.xc.usercentor.service.UserService;
import com.xc.usercentor.excption.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Version;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.xc.usercentor.constant.UserConstant.ADMIN_ROLE;
import static com.xc.usercentor.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户注册实现类
 *
 * @author zxl
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 盐值， 混淆密码
     */
    private static final String SALT = "xc";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
//            return -1L;
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码或者确认密码小于8位");
        }

        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "平台编号大于5位");
        }

        //账户不能包括特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }

        //密码不能包含特殊字符
        matcher = Pattern.compile(validPattern).matcher(userPassword);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码包含特殊字符");
        }

        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码和确认密码不一致");
        }

        //账户不能重复 如果已经有特殊字符，就省去一次数据库查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已经存在");
        }

        // 平台编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "平台编号已经存在");
        }

        // 2. 对密码加密 md5单向加密，不需要解密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存操作失败");
        }
        // 如果返回null long装箱失败， Long才行
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "账号或密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度小于4位，最小为4位");
        }


        //账户不能包括特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }

        // 2. 对密码加密 md5单向加密，不需要解密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //账户不能重复 如果已经有特殊字符，就省去一次数据库查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = (User) userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.error("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户密码不匹配");
        }
        // 3.用户脱敏
        User safetyUser = getSafetyUser(user);

        // 4. 记录用户的登录态 如何知道哪个用户登录呢？
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "原始模型数据为NULL");
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setTags(originUser.getTags());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /***
     * search user by tags （内存查询）
     * @param tagNameLists
     * @return
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagNameLists) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        return userList.stream().filter(user -> {
            String tagsStr = user.getTags();
            if (StringUtils.isBlank(tagsStr)) {
                return false;
            }
            Set<String> tempTagNameList = gson.fromJson(tagsStr, new TypeToken<Set<String>>() {
            }.getType());
            tempTagNameList = Optional.ofNullable(tempTagNameList).orElse(new HashSet<>());
            for (String tagName : tagNameLists) {
                if (!tempTagNameList.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public Integer updateUser(User user, User loginUser) {
        long userId = user.getId();
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //如果是管理员，允许更新任意用户
        //如果不是管理员，只允许更新当前信息
        if (isAdmin(loginUser) && userId != loginUser.getId()) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        //判断权限，仅管理员和自己能修改
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return (User) userObj;
    }


    /**
     * 是否为管理员  是-true
     *
     * @param servletRequest
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest servletRequest) {
        Object userObj = servletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return (user != null && user.getUserRole() == ADMIN_ROLE);
    }

    /**
     * 是否为管理员  是-true
     *
     * @param loginUser
     * @return
     */
    @Override
    public boolean isAdmin(User loginUser) {
        return loginUser != null && loginUser.getUserRole() == ADMIN_ROLE;
    }

    /***
     * search user by tags sql版
     * @param tagNameLists
     * @return
     */
    @Deprecated
    private List<User> searchUsersByTagsBySQL(List<String> tagNameLists) {
        if (CollectionUtils.isEmpty(tagNameLists)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (String tagName : tagNameLists) {
            queryWrapper = queryWrapper.like("tags", tagName);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }
}




