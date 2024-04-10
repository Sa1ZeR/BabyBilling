package com.nexign.babybilling.callgenerator.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisLock {

    private final RedisTemplate<String, Long> redisTemplate;

    /**
     * метод запроса блокировки
     * @param key - ключ объекта
     * @param endDate время окончания блокировки
     * @return статус блокировки (true активна)
     */
    public boolean acquireLock(String key, long endDate) {
        String lockKey = getLockKey(key);

        Long expiredLock = redisTemplate.opsForValue().get(lockKey);

        //если запись есть, то надо проверить ее состояние
        if(expiredLock != null) {
            if(endDate > expiredLock) releaseLock(lockKey); //если блокировка просрочена, то удаляем
            else return false; //в противном случае такой объект заблокировать нельзя
        }

        //устанавливаем блокировку
        return Optional.ofNullable(redisTemplate.opsForValue().setIfAbsent(lockKey, endDate)).orElse(false);
    }

    /**
     * освобождение блокировки
     * @param key ключ, по которому надо удалить блокировку
     * @return статус
     */
    public boolean releaseLock(String key) {
        String lockKey = getLockKey(key);

        return Optional.ofNullable(redisTemplate.delete(lockKey)).orElse(false);
    }

    private String getLockKey(String key) {
        return String.format("key:%s", key);
    }
}
