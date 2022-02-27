package com.qu.lele.service.impl;

import com.qu.lele.service.PlaceOrderService;
import com.qu.lele.service.RedLockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author: 屈光乐
 * @create: 2022-02-26 17-39
 * 红锁实现分布式锁
 */
@Service
@Slf4j
public class RedLockServiceImpl implements RedLockService {
    @Autowired
    private PlaceOrderService placeOrderService;
    @Autowired
    @Qualifier("redissonClient1")
    private RedissonClient redissonClient1;
    @Autowired
    @Qualifier("redissonClient2")
    private RedissonClient redissonClient2;
    @Autowired
    @Qualifier("redissonClient3")
    private RedissonClient redissonClient3;
    @Autowired
    @Qualifier("redissonClient4")
    private RedissonClient redissonClient4;
    @Autowired
    @Qualifier("redissonClient5")
    private RedissonClient redissonClient5;

    @Override
    public String obtainDistributedLock(String goodId, String userId) {
        log.debug("秒杀商品Id:{},用户Id:{}",goodId,userId);
        String key = goodId.intern();
        //5台机器必须满足3台机器以上能够获取红锁才算成功，否则获取红锁失败(过半原则)
        RLock lock1 = redissonClient1.getLock(key);
        RLock lock2 = redissonClient2.getLock(key);
        RLock lock3 = redissonClient3.getLock(key);
        RLock lock4 = redissonClient4.getLock(key);
        RLock lock5 = redissonClient5.getLock(key);
        RedissonRedLock redissonRedLock = new RedissonRedLock(lock1,lock2,lock3,lock4,lock5);
        //获取红锁 (此代码默认 设置key 超时时间30秒，过10秒，再延时)
        redissonRedLock.lock();
        try {
            boolean isSecKillSuccess = placeOrderService.placeOrder(goodId, userId);
            if (isSecKillSuccess) {
                log.debug("用户{} 秒杀成功",userId);
                return "Success";
            } else {
                log.debug("库存不足,用户{} 秒杀失败",userId);
                return "Fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redissonRedLock.unlock();
        }
        return "Fail";
    }
}
