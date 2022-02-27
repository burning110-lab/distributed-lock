package com.qu.lele.service;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 17-01
 */
public interface RedisLockService {

    public String obtainDistributedLock(String goodId,String userId);
}
