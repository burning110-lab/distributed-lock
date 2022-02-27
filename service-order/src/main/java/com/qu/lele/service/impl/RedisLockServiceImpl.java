package com.qu.lele.service.impl;

import com.qu.lele.entity.TblOrder;
import com.qu.lele.entity.TblOrderLock;
import com.qu.lele.lock.RedisLock;
import com.qu.lele.service.PlaceOrderService;
import com.qu.lele.service.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 屈光乐
 * @create: 2022-02-26 17-17
 * 单个redis实现分布式锁
 */
@Service
@Slf4j
public class RedisLockServiceImpl implements RedisLockService {
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private PlaceOrderService placeOrderService;

    @Override
    public String obtainDistributedLock(String goodId, String userId) {
        log.debug("商品Id[{}],用户Id[{}]",goodId,userId);
        //1.生成key
        TblOrderLock orderLock = new TblOrderLock();
        orderLock.setOrderId(Integer.valueOf(goodId));
        orderLock.setUserId(Integer.valueOf(userId));
        //lock
        redisLock.lock();
        try {
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

        }finally {
            redisLock.unlock();
        }
        return "Fail";
    }
}
