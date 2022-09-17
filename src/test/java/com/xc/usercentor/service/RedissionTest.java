package com.xc.usercentor.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RedissionTest {
    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {
        List<String> list = new ArrayList<>();
        list.add("yupi");
        System.out.println(list.get(0));
        list.remove(0);

        RList<String> rslist = redissonClient.getList("test-list");
//        rslist.add("yupi");
        System.out.println("rlist:" + rslist.get(0));
        rslist.remove(0);
    }
}
