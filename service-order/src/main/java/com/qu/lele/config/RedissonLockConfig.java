package com.qu.lele.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 16-03
 * 使用redisson配置
 */
@Configuration
public class RedissonLockConfig {

    @Bean
    public RedissonClient redissonClient() {
        RedissonClient redissonClient = Redisson.create();
        return redissonClient;
    }
}
