package com.card.cms.payload;

import com.card.cms.utils.payload.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TransactionResponse extends ApiResponse {

    private List<Transaction> transactions;

    public TransactionResponse(){}

    public TransactionResponse(boolean success, String message, List<Transaction> transactions){
        super(success, message);
        this.transactions = transactions;
    }
}
