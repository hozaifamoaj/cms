package com.card.cms.repository;


import com.card.cms.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT sender_card, ROUND(sum(transaction_amount),2) as amount, " +
            "EXTRACT (YEAR FROM TO_DATE(transaction_time,'yyyy-MM-dd')) AS YEAR, " +
            "EXTRACT (MONTH FROM TO_DATE(transaction_time,'yyyy-MM-dd')) AS MONTH, " +
            "EXTRACT (DAY FROM TO_DATE(transaction_time,'yyyy-MM-dd')) AS DAY " +
            "FROM TRANSACTION  " +
            "WHERE SENDER_CARD= ?1 " +
            "GROUP BY " +
            "    sender_card, YEAR,MONTH,DAY " +
            "ORDER BY " +
            "    sender_card, YEAR DESC,MONTH DESC,DAY DESC limit 1", nativeQuery=true)
    List<Object[]> getAmountForCardIdByDate(Integer cardId);

    @Query(value = "SELECT sender_card, ROUND(sum(transaction_amount),2) as amount, " +
            "EXTRACT (YEAR FROM TO_DATE(transaction_time,'yyyy-MM-dd')) AS YEAR, " +
            "EXTRACT (MONTH FROM TO_DATE(transaction_time,'yyyy-MM-dd')) AS MONTH " +
            "FROM TRANSACTION " +
            "WHERE SENDER_CARD= ?1 " +
            "GROUP BY " +
            "    sender_card, YEAR,MONTH " +
            "ORDER BY " +
            "    sender_card, YEAR DESC,MONTH DESC limit 1", nativeQuery=true)
    List<Object[]> getAmountForCardIdByMonth(Integer cardId);
}
