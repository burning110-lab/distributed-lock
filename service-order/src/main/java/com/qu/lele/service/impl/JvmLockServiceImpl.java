package com.qu.lele.service.impl;

import com.qu.lele.service.JvmLockService;
import com.qu.lele.service.PlaceOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: 屈光乐
 * @create: 2022-02-26 16-23
 * jvm实现本地锁,不适用于分布式环境
 */
@Service("jvmLockService")
@Slf4j
public class JvmLockServiceImpl implements JvmLockService {
    private static final Lock lock = new ReentrantLock();
    @Autowired
    private PlaceOrderService placeOrderService;

    @Override
    public String obtainDistributedLock(String goodId, String userId) {
        log.debug("商品Id[{}],用户Id[{}]",goodId,userId);
        try {
            boolean isLock = lock.tryLock(10, TimeUnit.SECONDS);
            if (isLock) {
                log.debug("用户[{}]获取锁成功!",userId);
                boolean b = placeOrderService.placeOrder(goodId, userId);
                if (b) {
                    log.debug("用户[{}]秒杀商品成功!",userId);
                    return "Success";
                } else {
                    log.debug("用户[{}]秒杀商品失败!",userId);
                    return "Fail";
                }
            } else {
                log.debug("用户[{}]获取锁失败!",userId);
                return "Fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail";
        }

    }
}
