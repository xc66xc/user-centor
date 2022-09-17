package com.xc.usercentor.service;

import com.xc.usercentor.model.domains.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Test
    void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("yupi","dog");
        valueOperations.set("yupiInt",1);
        valueOperations.set("yupiDouble",2.0);
        User user = new User();
        user.setId(1L);
        user.setUsername("yupi");
        valueOperations.set("yupiUser",user);

        Object yupi = valueOperations.get("yupi");
        Assertions.assertTrue("dog".equals((String)yupi));
        valueOperations.get("yupiInt");
    }
}
