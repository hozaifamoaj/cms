package com.card.cms.utils.enums;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum CardType implements Serializable {
    Prepaid(1, "Prepaid"),
    Postpaid(2,"Postpaid");

    CardType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    private final Integer value;
    private final String name;

    public static CardType getCardfromId(final int type) {
        for (final CardType s : CardType.values()) {
            if (s.value == type) {
                return s;
            }
        }
        return null;
    }
}
