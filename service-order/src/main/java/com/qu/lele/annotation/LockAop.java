package com.qu.lele.annotation;

import com.qu.lele.constant.RedisKeyConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author: 屈光乐
 * @create: 2022-02-24 20-31
 * 利用aop封装公共获取分布式方式
 */
@Component
@Aspect
@Slf4j
public class LockAop {
    private WebApplicationContext webApplicationContext;
    
    public LockAop(WebApplicationContext webApplicationContext) {
     
        this.webApplicationContext = webApplicationContext;
    }
    
    @Pointcut("@annotation(com.qu.lele.annotation.DistributedLock)")
    private void apiAop() {
        
    }

    @Around(value = "apiAop()")
    public Object roundAop(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Object[] args = proceedingJoinPoint.getArgs();
        Method method = methodSignature.getMethod();
        DistributedLock distributedLock  = method.getAnnotation(DistributedLock.class);
        RedisLockRegistry redisLockRegistry = (RedisLockRegistry) webApplicationContext.getBean(distributedLock.value());

        Lock lock = redisLockRegistry.obtain(RedisKeyConstant.RedisLockRegistryKey);
        boolean isLock;
        try {
            int retry = distributedLock.retry();
            if (retry > 0) {
                for (int i = 0; i < retry; i++) {
                    isLock = lock.tryLock(distributedLock.timeout(), TimeUnit.SECONDS);
                    if (isLock) {
                        log.info("用户[{}]抢到锁",args[1]);
                        break;
                    } else {
                        continue;
                    }
                }
            } else {
                isLock = lock.tryLock(distributedLock.timeout(), TimeUnit.SECONDS);
                if (isLock) {
                    log.info("用户[{}]抢到锁",args[1]);
                }
            }
        } catch (Exception e) {
            log.info("用户[{}]抢锁失败",args[1]);
            e.printStackTrace();
        } catch (Throwable throwable) {
            log.info("用户[{}]抢锁失败",args[1]);
            throwable.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }
}
