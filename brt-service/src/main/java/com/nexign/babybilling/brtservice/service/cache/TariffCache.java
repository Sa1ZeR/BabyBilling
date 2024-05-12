package com.nexign.babybilling.brtservice.service.cache;

import com.nexign.babybilling.brtservice.entity.Tariff;
import com.nexign.babybilling.brtservice.mapper.TariffMapper;
import com.nexign.babybilling.payload.dto.TariffDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TariffCache {

    private final TariffMapper tariffMapper;

    /**
     * Обновить тариф в кэше
     * @param tariff тариф
     * @return dto тарифа
     */
    @CachePut(cacheNames = "tariffCache", key = "#tariff.name")
    public TariffDto updateTariffCache(Tariff tariff) {
        return tariffMapper.map(tariff);
    }
}
