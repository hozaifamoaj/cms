package com.card.cms.utils.enums;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum TransactionType implements Serializable {
    Recharge(1,"Recharge"),
    P2P (2,"P2P"),
    Withdraw (3,"Withdraw");


    TransactionType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    private final Integer value;
    private final String name;

    public static TransactionType getTypefromName(final String type) {
        for (final TransactionType s : TransactionType.values()) {
            if (s.name.equalsIgnoreCase(type)) {
                return s;
            }
        }
        return null;
    }

    public static TransactionType getTypefromValue(final Integer value) {
        for (final TransactionType s : TransactionType.values()) {
            if (s.value == value) {
                return s;
            }
        }
        return null;
    }
}
