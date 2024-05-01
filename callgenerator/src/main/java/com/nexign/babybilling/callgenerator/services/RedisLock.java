package com.nexign.babybilling.callgenerator.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.KeyScanOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.*;

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
            if(endDate > expiredLock) releaseLock(key); //если блокировка просрочена, то удаляем
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

    /**
     * Удаляет все ключи по переданному префексу
     * @param prefix префикс ключа
     */
    public void releaseAllLocks(String prefix) {
        try (RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection()) {
            ScanOptions options = KeyScanOptions.scanOptions().match("*" + prefix + "*").build(); //маска, в начале есть спец символы, поэтому используем * в начале

            try(Cursor<byte[]> c = redisConnection.keyCommands().scan(options)) {
                while (c.hasNext()) {
                    redisConnection.keyCommands().del(c.next());
                }
            }
        }
    }

    private String getLockKey(String key) {
        return String.format("lock:%s", key);
    }
}
