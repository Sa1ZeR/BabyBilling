package com.nexign.babybilling.crmservice.service;

import com.nexign.babybilling.crmservice.config.property.ProducerProperty;
import com.nexign.babybilling.crmservice.payload.request.AuthRequest;
import com.nexign.babybilling.crmservice.payload.request.RegisterRequest;
import com.nexign.babybilling.crmservice.payload.response.AuthResponse;
import com.nexign.babybilling.crmservice.security.jwt.JwtTokenService;
import com.nexign.babybilling.payload.dto.CustomerDto;
import com.nexign.babybilling.payload.events.CreateNewCustomerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerService customerService;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProducerProperty producerProperty;

    /**
     * Производим авторизация пользователя
     * @param request запрос на авторизацию
     * @return jwt токен
     */
    public AuthResponse doAuth(AuthRequest request) {
        CustomerDto customer = customerService.findByMsisndAndPassword(request.msisnd(),
                bCryptPasswordEncoder.encode(request.password()));
        if(ObjectUtils.isEmpty(customer)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Неверный логин или пароль!");

        String jwt = jwtTokenService.generateToken(customer);
        return new AuthResponse(jwt);
    }

    /**
     * Регистрация нового пользователя
     * @param request запрос на регистрацию
     * @return статус ответа
     */

    public String register(RegisterRequest request) {
        CustomerDto customer = customerService.findByMsisnd(request.msisnd());
        if(!ObjectUtils.isEmpty(customer)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Такой абонент уже существует");

        try {
            SendResult<String, Object> res = kafkaTemplate.send(producerProperty.crmTopicName, request.msisnd(),
                    CreateNewCustomerEvent.builder()
                            .msisnd(request.msisnd())
                            .passwrd(bCryptPasswordEncoder.encode(request.password()))
                            .tariff(null) //brt должен будет установить дефолтный тариф
                            .balance(100) //новым пользователям ставим тестовый баланс
                            .build()).get();
            log.info("Successfully sent CreateCustomerEvent: {}-{}", res.getRecordMetadata().topic(), res.getRecordMetadata().partition());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while sending CreateCustomerEvent", e);
        }

        return "Успешная регистрация";
    }
}
