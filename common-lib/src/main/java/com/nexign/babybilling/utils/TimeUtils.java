package com.nexign.babybilling.utils;

import com.nexign.babybilling.domain.Pair;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@UtilityClass
public class TimeUtils {
    /**
     * Преобразовывает промежуток времени в формат hh:mm:ss
     * @param duration время в секундах
     * @return формат вывода промежутка времени hh:mm:ss
     */
    public static String getDurationString(long duration) {
        long diffSeconds = duration % 60;
        long diffMinutes = duration / 60 % 60;
        long diffHours = duration / (60 * 60) % 24;

        return String.format("%s:%s:%s",
                diffHours < 10 ? "0" + diffHours : diffHours,
                diffMinutes < 10 ? "0" + diffMinutes : diffMinutes,
                diffSeconds < 10 ? "0" + diffSeconds : diffSeconds);
    }

    /**
     * Конвертирует LocalDateTime время в unix формат
     * @param localDateTime
     * @return unix время
     */
    public static long toUnixTime(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    /**
     * конвертирует время формата unix в LocalDateTime
     * @param unix время unix формата
     * @return время в LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(long unix) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unix), ZoneId.systemDefault());
    }

    /**
     * Конвертирует время в год - месяц
     * @param unix время в unix формате
     * @return Pair, где 1 = год, 2 = месяц
     */
    public static Pair<Integer, Integer> toPair(long unix) {
        LocalDateTime localDateTime = toLocalDateTime(unix);
        return new Pair<>(localDateTime.getYear(), localDateTime.getMonthValue());
    }
}
