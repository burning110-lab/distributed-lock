package com.qu.lele.lock;
import com.qu.lele.entity.TblOrder;
import com.qu.lele.entity.TblOrderLock;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 17-09
 * redis实现分布式锁
 */
@Component
@Slf4j
@Data
public class RedisLock implements Lock {
    @Autowired
    @Qualifier("delLockByLua")
    private DefaultRedisScript<Boolean> defaultRedisScript;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private TblOrderLock tblOrderLock;

    /**
     * 递归方式获取加锁
     */
    @Override
    public void lock() {
       //1.尝试获取锁
        if (tryLock()) {
            return;
        }
        //2.睡眠
        try {
            Thread.sleep(10);
        } catch (Exception e ){

        }
        //3.递归调用
        lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    /**
     * 非阻塞加锁，成功就成功，失败就失败
     * @return
     */
    @Override
    public boolean tryLock() {
        //这里也可以利用lua脚本保证原子性操作
        boolean b = stringRedisTemplate.opsForValue().setIfAbsent(String.valueOf(tblOrderLock.getOrderId())
                , String.valueOf(tblOrderLock.getUserId()), 10, TimeUnit.SECONDS).booleanValue();
        if (b) {
            log.debug("用户[{}]加锁成功");
        } else {
            log.debug("用户[{}]加锁失败");
        }
        return b;
    }

    /**
     * 尝试在指定时间内获取锁,成功就成功，失败就失败
     * @param time 尝试获取加锁时间
     * @param unit
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        boolean isLock = false;
        if (time > 0) {
            isLock = tryLock();
            return isLock;
        } else {
            long expiredTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(time,TimeUnit.SECONDS);
            while (expiredTime >= System.currentTimeMillis()) {
                if (!tryLock()) {
                    continue;
                } else {
                    isLock = true;
                    break;
                }
            }
        }
        return isLock;
    }

    /**
     * 释放锁
     */
    @Override
    public void unlock() {
        String orderId = String.valueOf(tblOrderLock.getOrderId());
        String userId = String.valueOf(tblOrderLock.getUserId());
        //利用lus脚本保证删除操作原子性，同时删除自己设置的那个key
        stringRedisTemplate.execute(defaultRedisScript, Arrays.asList(orderId),userId);
        log.debug("用户[{}]释放锁",userId);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
