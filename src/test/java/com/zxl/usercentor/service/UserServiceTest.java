package com.zxl.usercentor.service;

import com.zxl.usercentor.model.domains.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户服务测试
 *
 * @author zxl
 */
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setAvatarUrl("https://file.zsxq.com/4f4/c0/f4c04f27351408ed8a43b6b3b0626796fcd95059b3ef8afcb9075d37050586af_min.jpg");
        user.setUsername("zxl");
        user.setUserAccount("123");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("10086");
        user.setEmail("666@qq.com");
        boolean result = userService.save(user);
        System.out.println("result = " + result);
        System.out.println(user.getId());
        Assert.assertTrue(result);
    }

    @Test
    void userRegister() {
        String userAccount = "zxl";
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode = "0";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);


        userAccount = "xl";
        userPassword = "132456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "zxl zqm";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "zxlandzqm";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 确认密码不一致
        checkPassword = "123456789";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        // 账号大于4，密码大于8
        userAccount = "hahazxl";
        userPassword = "132456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "zxlzqm";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "namei";
        userPassword = "namei123";
        checkPassword = "namei123";
        planetCode = "5";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertTrue(result > 0);


    }
}