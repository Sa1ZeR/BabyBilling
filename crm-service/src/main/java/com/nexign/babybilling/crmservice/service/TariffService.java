package com.nexign.babybilling.crmservice.service;

import com.nexign.babybilling.domain.Constants;
import com.nexign.babybilling.payload.dto.TariffDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TariffService {

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, TariffDto> redisTemplate;

    @Cacheable(cacheNames = "tariffCache", key = "#name")
    public TariffDto findByName(String name) {
        try {
            ResponseEntity<TariffDto> response = restTemplate.exchange(String.format(Constants.BRT_SERVICE_URL + "/api/tariff/%s", name),
                    HttpMethod.GET, null, TariffDto.class);
            if(response.getStatusCode().value() / 100 != 2) {
                log.error("Error while getting tariff {}", response);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Тариф не найден");
            }
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Error while getting tariff {}", e.getMessage());
            return null;
        }
    }

    /**
     * Получаем все тарифы из кэша
     * @return список всех тарифов
     */
    public List<TariffDto> findAll() {
        Set<String> redisKeys = redisTemplate.keys("tariffCache*");

        //получаем ключи
        List<String> keysList = new ArrayList<>();
        for (String data : redisKeys) {
            keysList.add(data);
            System.out.println(data);
        }

        //получаем дто по ключу
        List<TariffDto> tariffList = new ArrayList<>();
        for (String k : keysList) {
            TariffDto dto = redisTemplate.opsForValue().get(k);
            if(dto == null) continue;
            tariffList.add(dto);
        }

        return tariffList;
    }
}
