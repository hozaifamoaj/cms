package com.card.cms.payload;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionLimit implements Serializable {

    Integer id;
    Float perTransactionLimit;
    Float dailyLimit;
    Float monthlyLimit;
    Integer version;

    public TransactionLimit(){}
    public TransactionLimit(com.card.cms.model.TransactionLimit limit) {
        if (limit != null) {
            this.id = limit.getId();
            this.dailyLimit = limit.getDailyLimit();
            this.monthlyLimit = limit.getMonthlyLimit();
            this.perTransactionLimit = limit.getPerTransactionLimit();
            this.version = limit.getVersion();
        }

    }

}
