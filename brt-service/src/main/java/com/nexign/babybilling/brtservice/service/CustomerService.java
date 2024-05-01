package com.nexign.babybilling.brtservice.service;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.mapper.CustomerTariffMapper;
import com.nexign.babybilling.brtservice.repo.CustomerRepo;
import com.nexign.babybilling.brtservice.repo.projection.CustomerDataProjection;
import com.nexign.babybilling.brtservice.repo.projection.CustomerTariffProjection;
import com.nexign.babybilling.payload.dto.CustomerDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo repo;
    private final BCryptPasswordEncoder encoder;
    private final CustomerTariffMapper tariffMapper;

    @Transactional
    public Customer save(Customer customer) {
        return repo.save(customer);
    }

    /**
     * Получение информации об абоненте (абонент + его тариф)
     * @param phone номер абонента
     * @return данные об абоненте
     */
    @Transactional(readOnly = true)
    public Optional<CustomerTariffProjection> findCustomerInfo(String phone) {
        return repo.findByCustomerInfo(phone);
    }

    /**
     * Получение абонента по его номеру
     * @param msisnd номер абонента
     * @return Customer
     */
    @Transactional(readOnly = true)
    public Customer findByMsisnd(String msisnd) {
        return repo.findByMsisnd(msisnd).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Customer with %s not found", msisnd)));
    }

    /**
     * Получение пользователя по его номеру и паролю (только в зашифрованном виде)
     * @param msisnd номер
     * @param password пароль (в зашифрованном виде)
     * @return Customer
     */
    public Customer findByMsisndAndPassword(String msisnd, String password) {
        Customer customer = repo.findByMsisnd(msisnd).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Customer with %s not found", msisnd)));
        //проверка пароля, так как bcrypt использует соль
        if (encoder.matches(password, customer.getPassword()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Customer with %s not found", msisnd));

        return customer;
    }

    /**
     * Получение всех абонентов
     * @return список абонентов
     */
    public List<Customer> findAll() {
        return repo.findAll();
    }

    /**
     * Получение информации по пользователю с его тарифом (используемые данные)
     * @param msisnd номер абонента
     * @param year год
     * @param month месяц
     * @return информация по пользователю
     */
    @Transactional
    public CustomerDataDto findCustomerData(String msisnd, Integer year, Integer month) {
        List<CustomerDataProjection> data = repo.findCustomerData(msisnd);
        if(data.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Данные не найдены для %s!", msisnd));

        Optional<CustomerDataProjection> first = data.stream().filter(d -> Objects.equals(d.getYear(), year) && d.getMonth().equals(month)).findFirst();

        if(first.isEmpty()) {
            CustomerDataProjection defaultData = data.stream().findFirst().get();

            return CustomerDataDto.builder()
                    .tariff(defaultData.getTariff())
                    .msisnd(defaultData.getMsisnd())
                    .year(year)
                    .month(month)
                    .tariffMinutesOther(defaultData.getTarMinOth())
                    .tariffMinutes(defaultData.getTarMin())
                    .minutesOther(0)
                    .minutes(0)
                    .build();
        }

        return tariffMapper.map(first.get());
    }
}
