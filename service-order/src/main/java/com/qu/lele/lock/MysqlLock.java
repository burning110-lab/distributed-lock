package com.qu.lele.lock;

import com.qu.lele.dao.TblOrderLockDao;
import com.qu.lele.entity.TblOrderLock;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 17-09
 * mysql实现分布式锁
 */
@Component
@Slf4j
@Data
public class MysqlLock implements Lock {
    @Autowired
    private TblOrderLockDao orderLockDao;

    private ThreadLocal<TblOrderLock> threadLocal;

    @Override
    public void lock() {
        //1.尝试去获取锁
        if (tryLock()) {
            log.debug("用户[{}]获取锁成功",threadLocal.get().getUserId());
            return;
        }
        //2.休眠
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //3.再次递归调用
        lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            TblOrderLock tblOrderLock = threadLocal.get();
            log.debug("获取锁的对象[{}]",threadLocal.get());
            orderLockDao.insert(tblOrderLock);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        boolean isLock = false;
        if (time > 0L) {
            isLock = tryLock();
            return isLock;
        } else {
            long expiredTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(time,unit);
            while(expiredTime >= System.currentTimeMillis()) {
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

    @Override
    public void unlock() {
        try {
            TblOrderLock tblOrderLock = threadLocal.get();
            orderLockDao.deleteByPrimaryKey(tblOrderLock.getOrderId());
            threadLocal.remove();
            log.debug("用户[{}]已经释放锁",tblOrderLock.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("释放锁失败!");
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
