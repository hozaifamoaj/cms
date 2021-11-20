package com.card.cms.payload;

import com.card.cms.utils.enums.TransactionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction implements Serializable {

    Integer id;
    Integer senderCard;
    Integer receiverCard;
    TransactionType transactionType;
    Date transactionTime;
    Float transactionAmount;

    public Transaction(com.card.cms.model.Transaction transactionEo){
        if (transactionEo!= null){
            this.id = transactionEo.getId();
            this.senderCard = transactionEo.getSenderCard();
            this.receiverCard = transactionEo.getReceiverCard();
            this.transactionType = TransactionType.getTypefromValue(transactionEo.getTransactionType());
            this.transactionTime = transactionEo.getTransactionTime();
            this.transactionAmount = transactionEo.getTransactionAmount();
        }
    }
}
