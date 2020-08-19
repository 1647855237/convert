package com.convert.convert.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: xiongwei
 * @Date: 2020/8/18
 * @why：
 */
@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    public void set(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }

    public Object get(String key){
       return redisTemplate.opsForValue().get(key);
    }


    /**
     * 增加(自增长), 负数则为自减
     *
     * @param key key值
     * @param increment 值
     * @return
     */
    public Long incrBy(String key, long increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }
}
