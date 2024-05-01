package com.nexign.babybilling.hrsservice.services;

import com.nexign.babybilling.domain.Constants;
import com.nexign.babybilling.payload.dto.CustomerDataDto;
import com.nexign.babybilling.payload.dto.TariffDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final RestTemplate restTemplate;

    /**
     * Получение данных о пользователе
     * @param msisnd номер абонента
     * @return данные о звонках, лимитах и прочее
     */
    @Cacheable(cacheNames = "customerDataCache", key = "#name")
    public CustomerDataDto findCustomerData(String msisnd, int year, int month) {
        try {
            ResponseEntity<CustomerDataDto> response = restTemplate.exchange(String.format(Constants.BRT_SERVICE_URL + "/api/customer/data/%s?year=%s&month=%s", msisnd, year, month),
                    HttpMethod.GET, null, CustomerDataDto.class);
            if(response.getStatusCode().value() / 100 != 2) {
                log.error("Error while getting CustomerDataDto {}", response);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
            }
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }
}
