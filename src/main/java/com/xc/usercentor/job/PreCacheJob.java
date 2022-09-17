package com.xc.usercentor.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xc.usercentor.excption.BusinessException;
import com.xc.usercentor.mapper.UserMapper;
import com.xc.usercentor.model.domains.User;
import com.xc.usercentor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热定时
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    private List<Long> mainUserList = Arrays.asList(1L);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Scheduled(cron = "0 39 0 * * *")
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock("xc:precachejob:docache:lock");
        try {
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock" + Thread.currentThread().getId());
                for (Long userId : mainUserList) {
                    //无缓存，查数据库
                    QueryWrapper<User> wrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), wrapper);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    //如果有缓存，直接查缓存
                    String redisKey = String.format("xc:user:recommed:%s", userId);
                    try {
                        valueOperations.set(redisKey, userPage, 1, TimeUnit.DAYS);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock" + Thread.currentThread().getId());
                lock.unlock();
            }
        }


    }
}
