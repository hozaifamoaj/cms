package com.card.cms.repository;

import com.card.cms.model.Card;
import com.card.cms.model.TransactionLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLimitRepository extends JpaRepository<TransactionLimit, Integer> {
}
