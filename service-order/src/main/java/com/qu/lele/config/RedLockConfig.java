package com.qu.lele.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 16-15
 * 红锁的配置
 */
@Configuration
public class RedLockConfig {

    @Bean
    @Primary
    @Qualifier("redissonClient1")
    public RedissonClient redissonClient1() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);
        return Redisson.create(config);
    }

    @Bean
    @Qualifier("redissonClient2")
    public RedissonClient redissonClient2() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6380").setDatabase(0);
        return Redisson.create(config);
    }

    @Bean
    @Qualifier("redissonClient3")
    public RedissonClient redissonClient3() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6381").setDatabase(0);
        return Redisson.create(config);
    }

    @Bean
    @Qualifier("redissonClient4")
    public RedissonClient redissonClient4() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6382").setDatabase(0);
        return Redisson.create(config);
    }

    @Bean
    @Qualifier("redissonClient5")
    public RedissonClient redissonClient5() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6383").setDatabase(0);
        return Redisson.create(config);
    }
}
