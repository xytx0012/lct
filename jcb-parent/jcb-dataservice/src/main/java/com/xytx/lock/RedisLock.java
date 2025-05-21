package com.xytx.lock;

import jakarta.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisLock {

    private static final String KEY_PREFIX = "lock:";
    private static final String ID_PREFIX = UUID.randomUUID() +"-";
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<>();
    static {
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    public static Boolean tryLock(String lockName,StringRedisTemplate stringRedisTemplate) {
        String threadId = ID_PREFIX+Thread.currentThread().getId();
       return stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX+lockName,threadId,1000, TimeUnit.SECONDS);
    }

    public static void unlock(String lockName,StringRedisTemplate stringRedisTemplate) {
        String threadId = ID_PREFIX+Thread.currentThread().getId();
        stringRedisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(KEY_PREFIX+lockName),threadId);
    }
}
