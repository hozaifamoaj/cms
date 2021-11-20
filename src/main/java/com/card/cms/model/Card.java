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
@Table(name = "card_info")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card extends DateUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "pan", nullable = false)
    Integer pan;

    @Column(name = "psn", nullable = false)
    Integer psn;

    @Column(name = "card_type", nullable = false)
    Integer cardType;

    @Column(name = "expiry_date", nullable = false)
    Date expiryDate;

    @Column(name = "balance", nullable = false, columnDefinition = "FLOAT(2)")
    Float balance;

    @Column(name = "status", nullable = false)
    Integer status;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL)
    TransactionLimit transactionLimit;

    @Version
    @Column(name = "version")
    Integer version;
}
