package com.nexign.babybilling.brtservice;

import com.nexign.babybilling.brtservice.service.CustomerTimeService;
import com.nexign.babybilling.brtservice.service.RedisTimeCache;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrtApp {

    private final RedisTimeCache redisTimeCache;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        redisTimeCache.releaseAllLocks("last_update");
        redisTimeCache.updateValue(CustomerTimeService.COMMON_KEY, 0L);
    }
}
