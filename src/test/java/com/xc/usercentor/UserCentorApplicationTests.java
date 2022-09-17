package com.xc.usercentor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class UserCentorApplicationTests {

    @Test
    void testDigest() {
        String newPassword= DigestUtils.md5DigestAsHex(("zxl"+"mypassword").getBytes());
        System.out.println("newPassword = " + newPassword);
    }

    @Test
    void contextLoads() {
    }

}
