package com.qu.lele.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author: 屈光乐
 * @create: 2022-02-25 16-45
 * 配置lua脚本实现redis添加和删除，保证操作原子性
 */
@Configuration
public class LuaRedisLockConfig {

   @Bean
   @Qualifier("setLockByLua")
   public DefaultRedisScript<Boolean> setLockByLua() {
       DefaultRedisScript<Boolean> defaultRedisScript =  new DefaultRedisScript<>();
       defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/luaScript/lock-set.lua")));
       defaultRedisScript.setResultType(Boolean.class);
       return defaultRedisScript;
   }

   @Bean
   @Qualifier("delLockByLua")
   public DefaultRedisScript<Boolean> delLockByLua() {
       DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
       defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/luaScript/lock-del.lua")));
       defaultRedisScript.setResultType(Boolean.class);
       return defaultRedisScript;
   }
}
