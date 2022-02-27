package com.qu.lele.service.impl;

import com.qu.lele.annotation.DistributedLock;
import com.qu.lele.service.PlaceOrderService;
import com.qu.lele.service.RedisLockRegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 屈光乐
 * @create: 2022-02-24 22-19
 * 利用Aop+RedisLockRegister来实现分布式锁
 */
@Service("redisLockRegisterService")
@Slf4j
public class RedisLockRegisterServiceImpl implements RedisLockRegisterService {
    @Autowired
    private PlaceOrderService placeOrderService;

    @Override
    @DistributedLock(value = "redisLockRegistry", timeout = 10,retry = 3)
    public String obtainDistributedLock(String goodId,String userId) {
        log.debug("秒杀商品Id:{},用户Id:{}",goodId,userId);
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
        }
        return "Fail";
    }
}
