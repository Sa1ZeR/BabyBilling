package com.nexign.babybilling.cdrservice.service.cdr;

import com.nexign.babybilling.payload.dto.CdrDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CdrBufferService {

    private PriorityBlockingQueue<CdrDto> priorityQueue = new PriorityBlockingQueue<>(100, Comparator.comparingLong(CdrDto::dateStart));

    public void addToBuffer(CdrDto cdrDto) {
        priorityQueue.add(cdrDto);//таким образом мы добавляем в очередь, которая будет сортировать наши объекты
    }

    /**
     * Получение из буффера список cdr записей
     * @param i - кол-во, которое надо достать из буффера
     * @return - список cdr записей
     */
    public List<CdrDto> getFromBuffer(int i) {
        List<CdrDto> list = new ArrayList<>(10);

        for(int j = 0; j < i; j++) {
            try {
                //если за указанное время мы не получим объект, то вернем список меньшего размера
                // (такое может случиться, если под конец тарификации закончились cdr)
                CdrDto take = priorityQueue.poll(35L, TimeUnit.SECONDS);

                if(take == null) return list;

                list.add(take);
            } catch (InterruptedException e) {
                return list;
            }
        }

        return list;
    }
}
