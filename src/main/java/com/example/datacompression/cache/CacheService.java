package com.example.datacompression.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

@Component
public class CacheService {
    @Autowired
    RedisTemplate<String, byte[]> redisTemplate;

    public void set (String key, Serializable value) {
        redisTemplate.opsForValue().set(key, serialize(value));
    }

    public <T> T  get (String key, Class<T> type) {
        return type.cast(deserialize(redisTemplate.opsForValue().get(key)));
    }

    public HashOperations opsForHash() {
        return redisTemplate.opsForHash();
    }

    public void  putAll (String key, Map<String, Object> map) {
        opsForHash().putAll(key, map);
    }

    public Object getHashValue(String key, String hashKey) {
        return opsForHash().get(key, hashKey);
    }
}
