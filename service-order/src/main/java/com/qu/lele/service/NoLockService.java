package com.qu.lele.service;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 17-05
 */
public interface NoLockService {

    public String obtainDistributedLock(String goodId,String userId);
}
