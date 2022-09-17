package com.xc.usercentor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.usercentor.common.BaseResponse;
import com.xc.usercentor.common.ErrorCode;
import com.xc.usercentor.common.ResultUtils;
import com.xc.usercentor.excption.BusinessException;
import com.xc.usercentor.model.domains.User;
import com.xc.usercentor.model.request.UserLoginRequest;
import com.xc.usercentor.model.request.UserRegisterRequest;
import com.xc.usercentor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.xc.usercentor.constant.UserConstant.ADMIN_ROLE;
import static com.xc.usercentor.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 * 响应类型：application/json
 *
 * @author zxl
 */
@RestController
@RequestMapping("user")
@CrossOrigin(origins = {"http://localhost:5173"},allowCredentials = "true")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.PARAMS_NULL_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();

        // 如果前端传过来的参数只要有一个为空，这里直接判断不用再去逻辑里面判断了
        // controller层倾向与请求参数本身的校验，不涉及业务逻辑本身
        // service层是倾向业务逻辑校验，只是加了一层校验对性能几乎没什么影响
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long userId = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(userId);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN, "账号未登录");
        }
        // TODO 校验用户是否合法
        User user = userService.getById(currentUser.getId());
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }


    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或者密码未输入");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 如果前端传过来的参数只要有一个为空，这里直接判断不用再去逻辑里面判断了
        // controller层倾向与请求参数本身的校验，不涉及业务逻辑本身
        // service层是倾向业务逻辑校验，只是加了一层校验对性能几乎没什么影响
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "用户密码未输入");
        }
        User user = userService.userLogin(userAccount, userPassword, httpServletRequest);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN, "账号未登录");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);

    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String userName, HttpServletRequest servletRequest) {
        if (!userService.isAdmin(servletRequest)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            wrapper.like("userName", userName);
        }
        List<User> userList = userService.list(wrapper);
        List<User> safetyList = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(safetyList);

    }

    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest servletRequest) {
        User loginUser = userService.getLoginUser(servletRequest);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //如果有缓存，直接查缓存
        String redisKey = String.format("xc:user:recommed:%s",loginUser.getId());
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        if (userPage != null) {
            return ResultUtils.success(userPage);
        }
        //无缓存，查数据库
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum,pageSize),wrapper);
        //写缓存
        try {
            valueOperations.set(redisKey,userPage,1, TimeUnit.DAYS);
        } catch (Exception e) {
            log.error("redis set key error",e);
        }
        return ResultUtils.success(userPage);

    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestParam long id, HttpServletRequest servletRequest) {
        if (!userService.isAdmin(servletRequest)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 逻辑删除！
        Boolean b = userService.removeById(id);
        return ResultUtils.success(b);

    }

    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        //1.校验参数是否为空
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.校验是否右权限
        User loginUser = userService.getLoginUser(request);

        //3.触发更新
        int result = userService.updateUser(user,loginUser);
        return ResultUtils.success(result);
    }


    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUsersByTags(tagNameList);
        return ResultUtils.success(userList);
    }


}
