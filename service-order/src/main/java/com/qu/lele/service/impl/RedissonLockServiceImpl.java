package com.qu.lele.service.impl;

import com.qu.lele.service.PlaceOrderService;
import com.qu.lele.service.RedissonLockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 屈光乐
 * @create: 2022-02-26 17-26
 * 单个redisson实现分布锁
 */
@Service
@Slf4j
public class RedissonLockServiceImpl implements RedissonLockService {
    @Autowired
    private PlaceOrderService placeOrderService;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public String obtainDistributedLock(String goodId, String userId) {
        log.debug("秒杀商品Id:{},用户Id:{}",goodId,userId);
        //1.根据key获取到lock
        RLock lock = redissonClient.getLock(goodId.intern());
        //2.尝试获取锁
        try {
            // 此代码默认 设置key 超时时间30秒，过10秒，再延时
            lock.lock();
            boolean b = placeOrderService.placeOrder(goodId, userId);
            if (b) {
                log.debug("用户[{}]秒杀商品成功!",userId);
                return "Success";
            } else {
                log.debug("用户[{}]秒杀商品失败!",userId);
                return "Fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return "Fail";
    }
}
