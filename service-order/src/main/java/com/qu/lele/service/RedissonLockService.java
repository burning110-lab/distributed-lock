package com.qu.lele.service;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 16-58
 */
public interface RedissonLockService {

    public String obtainDistributedLock(String goodId,String userId);
}
