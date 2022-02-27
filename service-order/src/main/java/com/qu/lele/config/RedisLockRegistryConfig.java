package com.qu.lele.config;

import com.qu.lele.constant.RedisKeyConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * @author: 屈光乐
 * @create: 2022-02-24 20-54
 * 配置RedisLockRegistry实体类，可以指定redis中key以及有效期(默认60s)
 * 采用map来缓存生成锁，最大容量为100000，RedisLockRegistry实现分布式锁采用本地锁ReentrantLock+redis来做的
 */
@Configuration
public class RedisLockRegistryConfig {

    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory connectionFactory) {
        return new RedisLockRegistry(connectionFactory, RedisKeyConstant.RedisLockRegistryKey);
    }
}
