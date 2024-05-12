package com.nexign.babybilling.mapper;

public interface Mapper<F, T> {

    /**
     * Преобразование из одного объекта в другой
     * @param from из какого объекта
     * @return в какой объект
     */
    T map(F from);
}
