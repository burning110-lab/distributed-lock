package com.qu.lele.service;

/**
 * @author: 屈光乐
 * @create: 2022-02-24 22-16
 */
public interface RedisLockRegisterService {

    public String obtainDistributedLock(String goodId,String userId);
}
