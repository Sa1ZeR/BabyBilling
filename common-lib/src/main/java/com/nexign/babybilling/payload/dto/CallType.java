package com.nexign.babybilling.payload.dto;

import lombok.Getter;

@Getter
public enum CallType {
    OUTGOING("01"), INCOMING("02");

    private final String code;

    CallType(String code) {
        this.code = code;
    }

    /**
     * Получить CallType по его строковому id
     * @param s строковое id (Например: 02)
     * @return CallType
     */
    public static CallType getType(String s) {
        for(CallType c : values()) {
            if(c.code.equals(s))
                return c;
        }
        throw new RuntimeException(String.format("CallType with code %s not found!", s));
    }

}
