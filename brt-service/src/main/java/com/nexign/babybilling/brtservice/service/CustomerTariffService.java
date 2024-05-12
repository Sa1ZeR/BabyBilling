package com.nexign.babybilling.brtservice.service;


import com.nexign.babybilling.brtservice.mapper.CustomerTariffMapper;
import com.nexign.babybilling.payload.dto.CustomerDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerTariffService {

    private final CustomerService customerService;
    private final CustomerTariffMapper customerTariffMapper;

    /**
     * Получаем данные по пользователю и его тарифу. Если данные есть в кэше, то получаем их
     * @param msisnd номер абонента
     * @param year - год
     * @param month - месяц
     * @return данные по пользователю
     */
    @Cacheable(cacheNames = "customerTariff", key = "#msisnd", unless = "#result == null")
    public CustomerDataDto findCustomerDataFromCache(String msisnd, Integer year, Integer month) {
        return customerService.findCustomerData(msisnd, year, month);
    }

    /**
     * Обновление данных в кэше
     * @return обновленное значение
     */
    @CachePut(cacheNames = "customerTariff", key = "#msisnd")
    public CustomerDataDto updateCustomerData(String msisnd, Integer year, Integer month) {
        return customerService.findCustomerData(msisnd, year, month);
    }
}
