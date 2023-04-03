package com.example.datacompression.rest;

import com.example.datacompression.cache.CacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

@RestController
@RequestMapping("cache/me")
public class CacheOpsController {
    private final CacheService cacheService;
    private final ObjectMapper mapper;

    public CacheOpsController(CacheService cacheService, ObjectMapper mapper) {
        this.cacheService = cacheService;
        this.mapper = mapper;
    }

    @PostMapping
    public void cacheData(@RequestBody Country country) throws IOException {
        cacheService.set("CN", country);
    }

    @PostMapping("/map")
    public void cacheMapData(@RequestBody Map<String, Object> request) {

        String key = new String((String) request.get("key"));
        request.remove("key");

        Map entries = cacheService.opsForHash().entries(key);
        if (entries != null) {
            request.keySet().forEach(k -> entries.putIfAbsent(k, request.get(k)));
            cacheService.opsForHash().putAll(key, entries);
        } else {
            cacheService.putAll(key, request);
        }

    }

    @GetMapping
    public Mono<ResponseEntity<Country>> getByKey(@RequestParam String key) {
        var res = cacheService.get(key, Country.class);
        return Mono.just(
                ResponseEntity.ok(res)
        );
    }

    @GetMapping("/map")
    public Mono<ResponseEntity<String>> getByKey(@RequestParam String key, @RequestParam String hk) {
        var res = cacheService.getHashValue(key, hk);
        return Mono.just(
                ResponseEntity.ok((String) res)
        );
    }
}


record Country (String name, int size, String code) implements Serializable {}