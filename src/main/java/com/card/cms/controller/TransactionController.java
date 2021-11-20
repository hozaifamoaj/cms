package com.card.cms.controller;

import com.card.cms.payload.TransactionResponse;
import com.card.cms.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping("/recharge/daily")
    public ResponseEntity makeRechargeDaily(@Valid @RequestBody com.card.cms.model.Transaction transaction) {
        TransactionResponse response = service.generateRechargeTransaction(transaction, "Recharge", "daily");
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getTransactions().get(0));
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    @PostMapping("/recharge/monthly")
    public ResponseEntity makeRecharge(@Valid @RequestBody com.card.cms.model.Transaction transaction) {
        TransactionResponse response = service.generateRechargeTransaction(transaction, "Recharge", "monthly");
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getTransactions().get(0));
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    @PostMapping("/p2p/daily")
    public ResponseEntity makePtoPDaily(@Valid @RequestBody com.card.cms.model.Transaction transaction) {
        TransactionResponse response = service.generateP2PTransaction(transaction, "P2P", "daily");
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getTransactions().get(0));
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    @PostMapping("/p2p/monthly")
    public ResponseEntity makePtoPMonthly(@Valid @RequestBody com.card.cms.model.Transaction transaction) {
        TransactionResponse response = service.generateP2PTransaction(transaction, "P2P", "monthly");
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getTransactions().get(0));
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    @PostMapping("/withdraw/daily")
    public ResponseEntity makeWithdrawDaily(@Valid @RequestBody com.card.cms.model.Transaction transaction) {
        TransactionResponse response = service.generateWithdrawTransaction(transaction, "Withdraw", "daily");
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getTransactions().get(0));
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    @PostMapping("/withdraw/monthly")
    public ResponseEntity makeWithdrawMonthly(@Valid @RequestBody com.card.cms.model.Transaction transaction) {
        TransactionResponse response = service.generateWithdrawTransaction(transaction, "Withdraw", "monthly");
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getTransactions().get(0));
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }
}
