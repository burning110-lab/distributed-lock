package com.qu.lele.service.impl;

import com.qu.lele.constant.RedisKeyConstant;
import com.qu.lele.service.PlaceOrderService;
import com.qu.lele.service.ZookeeperLockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: 屈光乐
 * @create: 2022-02-26 17-54
 * zookeeper实现分布式锁
 */
@Service
@Slf4j
public class ZookeeperLockServiceImpl implements ZookeeperLockService {
    @Autowired
    private PlaceOrderService placeOrderService;
    @Autowired
    private CuratorFramework curatorFramework; //zookeeper客户端
    @Override
    public String obtainDistributedLock(String goodId, String userId) {
        log.debug("秒杀商品Id:{},用户Id:{}",goodId,userId);
        //1.生成锁的路径，同一大把锁路径必须一致
        String lockPath = RedisKeyConstant.ZookeeperKeyPath + goodId;
        //2.创建分布式锁
        InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework,lockPath);
        try {
            //3.获取加锁资源
            if (interProcessMutex.acquire(10, TimeUnit.SECONDS)) {
                boolean isSecKillSuccess = placeOrderService.placeOrder(goodId,userId);
                if (isSecKillSuccess) {
                    log.debug("用户{} 秒杀成功",userId);
                    return "Success";
                } else {
                    log.debug("库存不足,用户{} 秒杀失败",userId);
                    return "Fail";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                interProcessMutex.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "Fail";
    }
}
