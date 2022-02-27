package com.qu.lele.service;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 16-59
 */
public interface ZookeeperLockService {

    public String obtainDistributedLock(String goodId,String userId);
}
