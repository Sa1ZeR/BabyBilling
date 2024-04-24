package com.nexign.babybilling.crmservice.facade;

import com.nexign.babybilling.crmservice.payload.request.CustomerChangeTariffRequest;
import com.nexign.babybilling.crmservice.payload.request.PaymentRequest;
import com.nexign.babybilling.crmservice.service.CustomerService;
import com.nexign.babybilling.payload.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class CustomerFacade {

    private final CustomerService customerService;

    /**
     * Проверка о налии пользователя и дальнейшее отправка события
     * @param msisnd номер абонента
     * @param request запрос пополнения (баланс)
     */
    public void payment(String msisnd, PaymentRequest request) {
        CustomerDto customer = customerService.findByMsisnd(msisnd);
        if(ObjectUtils.isEmpty(customer)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найдет");

        customerService.payment(msisnd, request.amount());
    }

    /**
     * смена тарифа пользователем
     * @param msisnd - номер абонента
     * @param request запрос на изменение нарифа
     */
    public void changeTarrif(String msisnd, CustomerChangeTariffRequest request) {
        CustomerDto customer = customerService.findByMsisnd(msisnd);
        if(ObjectUtils.isEmpty(customer)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найдет");

        //todo проверка тарифа

        customerService.changeTariff(msisnd, request.tariff());
    }
}
