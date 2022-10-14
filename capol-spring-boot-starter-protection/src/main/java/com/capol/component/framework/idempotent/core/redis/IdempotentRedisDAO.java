package com.capol.component.framework.idempotent.core.redis;

import com.capol.component.framework.config.CapolRedisTemplate;
import com.capol.component.framework.core.RedisKeyDefine;
import lombok.AllArgsConstructor;

import java.util.concurrent.TimeUnit;

import static com.capol.component.framework.core.RedisKeyDefine.KeyTypeEnum.STRING;

/**
 * 幂等 Redis DAO
 */
@AllArgsConstructor
public class IdempotentRedisDAO {

    private static final RedisKeyDefine IDEMPOTENT = new RedisKeyDefine("幂等操作",
            "idempotent:%s", // 参数为 uuid
            STRING, String.class, RedisKeyDefine.TimeoutTypeEnum.DYNAMIC);

    private final CapolRedisTemplate capolRedisTemplate;

    public Boolean setIfAbsent(String key, long timeout, TimeUnit timeUnit) {
        String redisKey = formatKey(key);
       return capolRedisTemplate.tryLock(redisKey, "幂等操作锁!", timeout);
    }

    private static String formatKey(String key) {
        return String.format(IDEMPOTENT.getKeyTemplate(), key);
    }

}
