package com.example.datacompression.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class CacheConfig {

    @Bean
    public RedisTemplate<String, byte[]> redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory());
        redisTemplate.setValueSerializer(new SnappyConfig<>());
        redisTemplate.setHashValueSerializer(new SnappyConfig<>());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public JedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory();
    }
}
