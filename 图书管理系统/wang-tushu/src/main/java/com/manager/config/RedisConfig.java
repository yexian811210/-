package com.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // key序列化
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // value序列化
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        // hash类型 key序列化
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // hash类型 value序列化
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);

        // 注入连接工厂
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
    @Bean
    public JedisPool jedisPool() {
        // 创建Jedis连接池配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 配置连接池的一些参数，例如最大连接数、最大空闲连接数等
        // poolConfig.setMaxTotal(10);
        // poolConfig.setMaxIdle(5);

        // 创建Jedis连接池
        JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379);
        // 如果Redis有设置密码，可以在此处进行认证
        // jedisPool.getResource().auth("your_password");
        return jedisPool;
    }
}
