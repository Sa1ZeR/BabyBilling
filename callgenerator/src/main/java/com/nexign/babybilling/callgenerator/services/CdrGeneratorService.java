package com.nexign.babybilling.callgenerator.services;

import com.nexign.babybilling.callgenerator.repository.CdrRepository;
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
    public CdrDto generateCdrRecord(LocalDateTime currentTime) {
        int duration = random.nextInt(3600); //продолжительность звонка
        LocalDateTime endTime = currentTime.plusSeconds(duration);
        CallType callType = CallType.values()[random.nextInt(CallType.values().length)]; //тип звонка
        long unixTimeStart = TimeUtils.toUnixTime(currentTime);
        long unixTimeEnd = TimeUtils.toUnixTime(endTime);
        String user1 = getFreeUser(unixTimeEnd);
        String user2 = getFreeUser(unixTimeEnd);

        return new CdrDto(callType, user1, unixTimeStart, unixTimeEnd, user2);
    }

    /**
     * Получает абонента, который в данный момент не разговаривает
     * @param unixTimeEnd - время окончания разговора
     * @return абонент
     */
    private String getFreeUser(long unixTimeEnd) {
        while (true) {
            //получаем любой номер
            String userTmp = allUsers.get(random.nextInt(allUsers.size()));

            //проверка - находится ли пользователь в кеше.
            //Так мы фиксируем абонентов, которые в данный момент уже разговаривают и мы не можем их использовать для генерации
            if(redisLock.acquireLock(userTmp, unixTimeEnd))
                return userTmp;
        }
    }
}
