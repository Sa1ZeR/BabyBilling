package com.nexign.babybilling.brtservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.KeyScanOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisTimeCache {

    private final RedisTemplate<String, Integer> redisTemplate;

    /**
     * метод сохранения данных в кэш
     * @param key - ключ объекта
     * @param date время окончания блокировки
     * @return статус блокировки (true активна)
     */
    public boolean updateValue(String key, Long date) {
        String lockKey = getStorageKey(key);

        Integer expiredLock = redisTemplate.opsForValue().get(lockKey);

        //если запись есть, то надо сравнить для безопасности
        if(expiredLock != null) {
            if(date > expiredLock) releaseLock(key); //если блокировка просрочена, то удаляем
            else return false;
        }

        //устанавливаем блокировку
        return Optional.ofNullable(redisTemplate.opsForValue().setIfAbsent(lockKey, date.intValue())).orElse(false);
    }

    /**
     * освобождение блокировки
     * @param key ключ, по которому надо удалить блокировку
     * @return статус
     */
    public boolean releaseLock(String key) {
        String lockKey = getStorageKey(key);

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

    private String getStorageKey(String key) {
        return String.format("last_update:%s", key);
    }

    public Integer getValueByKey(String key) {
        String lockKey = getStorageKey(key);

        return redisTemplate.opsForValue().get(lockKey);
    }
}
