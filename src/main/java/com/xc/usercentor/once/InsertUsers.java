package com.xc.usercentor.once;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xc.usercentor.mapper.UserMapper;
import com.xc.usercentor.model.domains.User;
import com.xc.usercentor.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

@Component
public class InsertUsers {
    @Resource
    public UserService userService;

//    @Scheduled(initialDelay = 5000, fixedRate = Long.MAX_VALUE)
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000000;
        List<User> list = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setAvatarUrl("https://file.zsxq.com/4f4/c0/f4c04f27351408ed8a43b6b3b0626796fcd95059b3ef8afcb9075d37050586af_min.jpg");
            user.setTags("");
            user.setUsername("假xc");
            user.setUserAccount("fakeXC"+i);
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("123");
            user.setEmail("213@qq.com");
            user.setPlanetCode(String.valueOf(6+i));
            user.setUserStatus(0);
            user.setUserRole(0);
            list.add(user);
        }
        userService.saveBatch(list,20);
        stopWatch.stop();
        System.out.printf("", stopWatch.getTotalTimeMillis());
    }

    public static void main(String[] args) {
        InsertUsers insertUsers = new InsertUsers();
        insertUsers.doInsertUsers();
    }
}
