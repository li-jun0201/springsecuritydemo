package org.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

/**
 *  redis service
 *
 *  @Author Joey Li
 *  @Data 2018-05-23
 */
@Slf4j
@Service
public class RedisService {

    @Value("${jwt.token.expire}")
    private Integer expireTime =100000;

    @Autowired
    StringRedisTemplate redisTemplate;

    public Boolean isTokenExpire(String token) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            byte[] expire = connection.get(token.getBytes());
            log.debug("Token:{} Expire:{}", token, expire == null);
            return expire == null;
        });
    }

    public void setToken(String token) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.set(token.getBytes(), LocalDateTime.now().plusSeconds(expireTime).toString().getBytes());
            connection.expire(token.getBytes(), expireTime);
            return null;
        });
    }

    public void setKeyValue(String key, String value, int expireTime) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.set(key.getBytes(), value.getBytes());
            connection.expire(key.getBytes(), expireTime);
            return null;
        });

    }

    public void setKeyValue(String key, String value) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.set(key.getBytes(), value.getBytes());
            return null;
        });
    }

    public String getValue(String key) {
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            byte[] bytes = connection.get(key.getBytes());
            return bytes == null ? null : new String(bytes);
        });
    }

    public void rpushKeyList(int expireTime, String key, String... values) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            byte[][] valueBytes = new byte[values.length][];
            for (int i = 0; i < valueBytes.length; i++) {
                valueBytes[i] = values[i].getBytes();
            }
            connection.rPush(key.getBytes(), valueBytes);
            connection.expire(key.getBytes(), expireTime);
            return null;
        });
    }

    public String lpopKeyList(String key) {
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            byte[] bytes = connection.lPop(key.getBytes());
            return bytes == null ? null : new String(bytes);
        });
    }

    public Long delKey(String key) {
        return redisTemplate.execute((RedisCallback<Long>) connection ->
                connection.del(key.getBytes()));
    }

    public Set<byte[]> keys(String pattern) {
        return redisTemplate.execute((RedisCallback<Set<byte[]>>) connection ->
                connection.keys(pattern.getBytes()));
    }

}
