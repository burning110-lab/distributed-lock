package com.qu.lele.service.impl;

import com.qu.lele.entity.TblOrderLock;
import com.qu.lele.lock.MysqlLock;
import com.qu.lele.service.MysqlLockService;
import com.qu.lele.service.PlaceOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: 屈光乐
 * @create: 2022-02-26 16-52
 * mysql实现分布式锁
 */
@Service("mysqlLockService")
@Slf4j
public class MysqlLockServiceImpl implements MysqlLockService {
    @Autowired
    private MysqlLock mysqlLock;
    @Autowired
    private PlaceOrderService orderService;
    private ThreadLocal<TblOrderLock> threadLocal = new ThreadLocal<>();

    @Override
    public String obtainDistributedLock(String goodId, String userId) {
        log.debug("商品Id[{}],用户Id[{}]",goodId,userId);
        //1.生成key
        TblOrderLock orderLock = new TblOrderLock();
        orderLock.setOrderId(Integer.valueOf(goodId));
        orderLock.setUserId(Integer.valueOf(userId));
        //2.将生成key存放到当前线程内存中
        threadLocal.set(orderLock);
        mysqlLock.setThreadLocal(threadLocal);
        //3.lock
        mysqlLock.lock();
        try {
            boolean b = orderService.placeOrder(goodId, userId);
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
            mysqlLock.unlock();
        }
        return "Fail";
    }
}
