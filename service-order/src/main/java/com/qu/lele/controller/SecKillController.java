package com.qu.lele.controller;

import com.qu.lele.service.RedisLockRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 屈光乐
 * @create: 2022-02-24 21-32
 */
@RestController
@RequestMapping(value = "secKill")

public class SecKillController {
    @Autowired
    private RedisLockRegisterService lockRegisterService;

    @RequestMapping(value = "inventory/{goodId}/{userId}")
    public String secKill(@PathVariable("goodId") String goodId,
                          @PathVariable("userId") String userId) {

        return lockRegisterService.obtainDistributedLock(goodId,userId);
    }
}
