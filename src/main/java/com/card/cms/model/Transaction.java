package com.card.cms.model;

import com.card.cms.utils.payload.DateUpdate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "transaction")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction extends DateUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "sender_card", nullable = false)
    Integer senderCard;

    @Column(name = "receiver_card", nullable = false)
    Integer receiverCard;

    @Column(name = "transaction_type")
    Integer transactionType;

    @Column(name = "transaction_time")
    Date transactionTime;

    @Column(name = "transaction_amount", nullable = false, columnDefinition = "FLOAT(2)")
    Float transactionAmount;

}
