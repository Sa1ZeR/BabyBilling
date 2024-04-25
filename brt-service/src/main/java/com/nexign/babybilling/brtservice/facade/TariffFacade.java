package com.nexign.babybilling.brtservice.facade;

import com.nexign.babybilling.brtservice.entity.Tariff;
import com.nexign.babybilling.brtservice.service.cache.TariffCache;
import com.nexign.babybilling.brtservice.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffFacade {

    private final TariffService tariffService;
    private final TariffCache tariffCache;

    /**
     * Занесение всех тарифов в кэш при запуске приложения
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    public void onStarted() {
        List<Tariff> all = tariffService.findAll();
        all.forEach(tariffCache::updateTariffCache);
    }
}
