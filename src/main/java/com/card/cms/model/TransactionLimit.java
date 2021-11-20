package com.card.cms.model;

import com.card.cms.utils.payload.DateUpdate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "transaction_limit")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionLimit extends DateUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @OneToOne
    @JoinColumn(name = "card_id")
    Card card;

    @Column(name = "per_transaction_limit", nullable = false, columnDefinition = "FLOAT(2)")
    Float perTransactionLimit;

    @Column(name = "daily_limit", nullable = false, columnDefinition = "FLOAT(2)")
    Float dailyLimit;

    @Column(name = "monthly_limit", nullable = false, columnDefinition = "FLOAT(2)")
    Float monthlyLimit;

    @Version
    @Column(name = "version")
    Integer version;
}
