package com.nexign.babybilling.brtservice.service;

import com.nexign.babybilling.brtservice.config.property.KafkaProducerProperty;
import com.nexign.babybilling.brtservice.mapper.CdrMapper;
import com.nexign.babybilling.brtservice.repo.projection.CustomerTariffProjection;
import com.nexign.babybilling.payload.dto.CallType;
import com.nexign.babybilling.payload.dto.CdrDto;
import com.nexign.babybilling.payload.dto.CdrPlusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrtService {

    private final CustomerService customerService;
    private final CdrMapper cdrMapper;
    private final KafkaTemplate<String, CdrPlusEvent> kafkaTemplate;
    private final KafkaProducerProperty producerProperty;

    /**
     * Преобразовывает полученные данные в файл и считывает его
     * @param data - данные формата Base64
     * @return список cdr из файла
     */
    public List<CdrDto> readCdrFile(String data) {
        try {
            //Создание временного файла и запись полученных данных
            Path tempFile = Files.createTempFile(UUID.randomUUID().toString(), "tmp");
            Files.write(tempFile, Base64.getDecoder().decode(data));

            //Считываем данные из файла
            return Files.readAllLines(tempFile)
                    .stream().map(cdrMapper::map).toList();
        } catch (Exception e) {
            log.error("Error file handling file packet:", e);
        }
        return Collections.emptyList();
    }
    
    @Transactional
    public List<CdrPlusEvent> filterCdrData(Collection<CdrDto> data) {
        List<CdrPlusEvent> list = new ArrayList<>();

        for(CdrDto cdr : data) {
            String phone1 = cdr.servedMsisnd(); //первый абонент
            String phone2 = cdr.contactedMsisnd(); //Второй абонент

            //получаем информацию о абоненте и его тарифе, если данные не получены, то данный абонент не является оператором ромашки
            Optional<CustomerTariffProjection> customer1 = customerService.findCustomerInfo(phone1);
            Optional<CustomerTariffProjection> customer2 = customerService.findCustomerInfo(phone2);

            //преобразовываем полученную информацию
            //мы должны проверять обе записи, так как бывают случае, что звонки могут быть от разных абонентов (в обе стороны)
            //для второго абонента мы должны обязательно поменять тип звонка
            customer1.ifPresent(tariffInfo -> list.add(createCdrPlus(tariffInfo, cdr, cdr.callType(), customer2.isPresent())));
            customer2.ifPresent(tariffInfo -> list.add(createCdrPlus(tariffInfo, cdr, CallType.swapCall(cdr.callType()), customer1.isPresent())));
        }

        return list;
    }

    /**
     * Создание CdrPlus
     * @param customer данные об абоненте вместе с его тарифом
     * @param cdr cdr запись
     * @param isSameOperator - одинаковые ли операторы у абонентов
     * @return CdrPlus запись
     */
    private CdrPlusEvent createCdrPlus(CustomerTariffProjection customer, CdrDto cdr, CallType callType, boolean isSameOperator) {
        return CdrPlusEvent.builder()
                .servedMsisnd(customer.getMsisnd())
                .callType(callType)
                .dateStart(cdr.dateStart())
                .dateEnd(cdr.dateEnd())
                .contactedWithSameOp(isSameOperator)
                .tariff(customer.getTariff())
                .build();
    }

    /**
     * Отправка события на расчет в brt
     * @param list список cdrplus записейй
     */
    public void calculateCustomers(Collection<CdrPlusEvent> list) {
        for(CdrPlusEvent event : list) {
            try {
                SendResult<String, CdrPlusEvent> result = kafkaTemplate.send(producerProperty.hrsTopic, event.servedMsisnd(), event).get();
                log.info("Successfully sent cdr plus data {}-{}", result.getRecordMetadata().topic(), result.getRecordMetadata().partition());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Can't send CdrPlus data: ", e);
            }
        }
    }
}
