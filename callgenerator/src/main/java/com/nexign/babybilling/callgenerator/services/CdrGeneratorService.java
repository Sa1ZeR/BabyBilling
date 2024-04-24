package com.nexign.babybilling.callgenerator.services;

import com.nexign.babybilling.payload.dto.CallType;
import com.nexign.babybilling.payload.dto.CdrDto;
import com.nexign.babybilling.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CdrGeneratorService {

    private final RedisLock redisLock;
    private final List<String> allUsers;
    private final Random random;

    /**
     * генерироует cdr
     *
     * @param currentTime - время начала звонка
     * @return cdr запись
     */
    public Optional<CdrDto> generateCdrRecord(LocalDateTime currentTime) {
        int duration = random.nextInt(3600); //продолжительность звонка
        LocalDateTime endTime = currentTime.plusSeconds(duration);
        CallType callType = CallType.values()[random.nextInt(CallType.values().length)]; //тип звонка

        long unixTimeStart = TimeUtils.toUnixTime(currentTime);
        long unixTimeEnd = TimeUtils.toUnixTime(endTime);

        Optional<String> user1 = getFreeUser(unixTimeEnd);
        Optional<String> user2 = getFreeUser(unixTimeEnd);
        if(user2.isEmpty() || user1.isEmpty()) return Optional.empty();

        return Optional.of(new CdrDto(callType, user1.get(), unixTimeStart, unixTimeEnd, user2.get()));
    }

    /**
     * Получает абонента, который в данный момент не разговаривает
     * @param unixTimeEnd - время окончания разговора
     * @return абонент
     */
    private Optional<String> getFreeUser(long unixTimeEnd) {
        int tryCount = 0; //количество попыток

        while (tryCount < 15) { //если за 15 попыток все абоненты заняты, то возвращаем пустого абонента
            //получаем любой номер
            String userTmp = allUsers.get(random.nextInt(allUsers.size()));

            //проверка - находится ли пользователь в кеше.
            //Так мы фиксируем абонентов, которые в данный момент уже разговаривают и мы не можем их использовать для генерации,
            // фича работает даже если мы будем масштабировать горизонтально данный сервис
            // + защита от звонков самому себе
            if(redisLock.acquireLock(userTmp, unixTimeEnd))
                return Optional.ofNullable(userTmp);

            tryCount++;
        }

        return Optional.empty();
    }
}
