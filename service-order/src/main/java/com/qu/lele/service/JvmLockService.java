package com.qu.lele.service;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 17-04
 */
public interface JvmLockService {

    public String obtainDistributedLock(String goodId,String userId);
}
