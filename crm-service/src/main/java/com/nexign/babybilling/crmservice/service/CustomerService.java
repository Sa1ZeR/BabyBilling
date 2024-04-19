package com.nexign.babybilling.crmservice.service;

import com.nexign.babybilling.domain.Constants;
import com.nexign.babybilling.payload.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * Получение абонента по номеру телефона
     * @param msisnd номер абонента
     * @return dto абонента {@link CustomerDto}
     */
    public CustomerDto findByMsisnd(String msisnd) {
        try {
            ResponseEntity<CustomerDto> response = restTemplate.exchange(String.format(Constants.BRT_SERVICE_URL + "/api/customer/%s", msisnd),
                    HttpMethod.GET, null, CustomerDto.class);
            if(response.getStatusCode().value() / 100 != 2) {
                log.error("Error while getting customer {}", response);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
            }
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    /**
     *
     * @param msisnd номер абонента
     * @param password пароль в зашифрованном виде
     * @return dto абонента {@link CustomerDto}
     */
    public CustomerDto findByMsisndAndPassword(String msisnd, String password) {
        try {
            ResponseEntity<CustomerDto> response = restTemplate.exchange(String.format(Constants.BRT_SERVICE_URL + "/api/customer/?msisnd=%s&password=%s", msisnd, password),
                    HttpMethod.GET, null, CustomerDto.class);
            if(response.getStatusCode().value() / 100 != 2) {
                log.error("Error while getting customer {}", response);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
            }
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error("Error while getting customer {}", e.getMessage());
            return null;
        }
    }
}
