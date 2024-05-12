package com.nexign.babybilling.callgenerator;

import com.nexign.babybilling.callgenerator.services.RedisLock;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CallGeneratorApp {

    private final RedisLock redisLock;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        redisLock.releaseAllLocks("lock:"); //удалить все значение из редиса, которые связаны блокировкой звонков (при старте приложения)
    }
}
