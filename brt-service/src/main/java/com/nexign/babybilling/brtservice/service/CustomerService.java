package com.nexign.babybilling.brtservice.service;

import com.nexign.babybilling.brtservice.entity.Customer;
import com.nexign.babybilling.brtservice.repo.CustomerRepo;
import com.nexign.babybilling.brtservice.repo.projection.CustomerTariffProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo repo;
    private final BCryptPasswordEncoder encoder;

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
}
