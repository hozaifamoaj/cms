package com.card.cms.payload;

import com.card.cms.utils.enums.CardType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card implements Serializable {

    Integer id;
    Integer pan;
    Integer psn;
    CardType cardType;
    Date expiryDate;
    Float balance;
    Integer status;
    TransactionLimit transactionLimit;
    Integer version;

    public Card(){}

    public Card(com.card.cms.model.Card cardEo) {
        if (cardEo!=null){
            this.id = cardEo.getId();
            this.pan = cardEo.getPan();
            this.psn = cardEo.getPsn();
            this.cardType = CardType.getCardfromId(cardEo.getCardType());
            this.expiryDate = cardEo.getExpiryDate();
            this.balance = cardEo.getBalance();
            this.status = cardEo.getStatus();
            this.transactionLimit = new TransactionLimit(cardEo.getTransactionLimit());
            this.version = cardEo.getVersion();
        }
    }
}
