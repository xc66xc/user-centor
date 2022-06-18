package com.zxl.usercentor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxl.usercentor.common.BaseResponse;
import com.zxl.usercentor.common.ErrorCode;
import com.zxl.usercentor.common.ResultUtils;
import com.zxl.usercentor.excption.BusinessException;
import com.zxl.usercentor.model.domains.User;
import com.zxl.usercentor.model.request.UserLoginRequest;
import com.zxl.usercentor.model.request.UserRegisterRequest;
import com.zxl.usercentor.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.zxl.usercentor.constant.UserConstant.ADMIN_ROLE;
import static com.zxl.usercentor.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 * 响应类型：application/json
 *
 * @author zxl
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

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
            return ResultUtils.error(ErrorCode.PARAMS_ERROR );
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
        if (!isAdmin(servletRequest)) {
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

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestParam long id, HttpServletRequest servletRequest) {
        if (!isAdmin(servletRequest)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 逻辑删除！
        Boolean b = userService.removeById(id);
        return ResultUtils.success(b);

    }

    /**
     * 是否为管理员  是-true
     *
     * @param servletRequest
     * @return
     */
    private boolean isAdmin(HttpServletRequest servletRequest) {
        Object userObj = servletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return (user != null && user.getUserRole() == ADMIN_ROLE);
    }
}
