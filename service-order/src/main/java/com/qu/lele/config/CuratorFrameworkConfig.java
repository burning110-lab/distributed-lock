package com.qu.lele.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 16-20
 * zookeeper客户端配置
 */
@Configuration
@Slf4j
public class CuratorFrameworkConfig {
    @Autowired
    private Environment environment;

    @Bean
    public CuratorFramework curatorFramework() {
        //ExponentialBackoffRetry是种重连策略，每次重连的间隔会越来越长,1000毫秒是初始化的间隔时间,3代表尝试重连次数。
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000,3);
        //创建客户端
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(environment.getProperty("zooClientIp"), retry);
        //添加事件回调
        curatorFramework.getCuratorListenable().addListener((CuratorFramework cf, CuratorEvent ce) -> {
            CuratorEventType type = ce.getType();
            if(type == CuratorEventType.WATCHED){
                WatchedEvent watchedEvent = ce.getWatchedEvent();
                String path = watchedEvent.getPath();
                log.debug("观察事件类型:{},变化的路径:{}",watchedEvent.getType(),path);
                // 重新设置改节点监听
                if(null != path){
                    cf.checkExists().watched().forPath(path);
                }
            }
        });
        //启动客户端
        curatorFramework.start();
        return curatorFramework;
    }
}
