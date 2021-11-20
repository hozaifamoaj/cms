package com.card.cms.service;

import com.card.cms.model.Card;
import com.card.cms.payload.Transaction;
import com.card.cms.payload.TransactionResponse;
import com.card.cms.repository.CardRepository;
import com.card.cms.repository.TransactionRepository;
import com.card.cms.utils.enums.TransactionType;
import com.card.cms.utils.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private KafkaService kafkaService;

    @Transactional
    public TransactionResponse generateP2PTransaction(com.card.cms.model.Transaction transactionEo, String trxType, String trxMode){
        com.card.cms.model.Card senderCardEo = null;
        com.card.cms.model.Card receiverCardEo = null;
        if(transactionEo != null) {
            try {
                if (transactionEo.getSenderCard() != null) {
                    senderCardEo = cardRepository.findById(transactionEo.getSenderCard())
                            .orElseThrow(() -> new Exception("No Data found sender card id: " + transactionEo.getSenderCard()));
                    receiverCardEo = cardRepository.findById(transactionEo.getReceiverCard())
                            .orElseThrow(() -> new Exception("No Data found receiver card id: " + transactionEo.getReceiverCard()));
                }
            } catch (Exception e) {
//            e.printStackTrace();
                return new TransactionResponse(false, e.getMessage(), new ArrayList<>());
            }
            if ( (Utils.isOk(senderCardEo) && senderCardEo.getStatus() == 1)
                    && (Utils.isOk(receiverCardEo) && receiverCardEo.getStatus() == 1)) {

                if (senderCardEo.getBalance() < transactionEo.getTransactionAmount()) {
                    return new TransactionResponse(false, "Insufficient balance: " +senderCardEo.getBalance(), new ArrayList<>());
                }

                if (senderCardEo.getTransactionLimit().getPerTransactionLimit() < transactionEo.getTransactionAmount()) {
                    return new TransactionResponse(false, "Requested amount " +transactionEo.getTransactionAmount()
                            + " is greater than per transaction uses amount " + senderCardEo.getTransactionLimit().getPerTransactionLimit(), new ArrayList<>());
                }

                Object[] limits = getLimits(senderCardEo);
                Float dailyLimit = (Float) limits[0];
                Float monthlyLimit = (Float) limits[1];

                if (trxMode.equals("daily")) {
                    if (dailyLimit >= transactionEo.getTransactionAmount()) {
                        senderCardEo.setBalance(senderCardEo.getBalance() - transactionEo.getTransactionAmount());
                        receiverCardEo.setBalance(receiverCardEo.getBalance() + transactionEo.getTransactionAmount());
                    } else {
                        return new TransactionResponse(false, "Requested amount " +transactionEo.getTransactionAmount()
                                + " is greater than remaining daily uses amount " + dailyLimit, new ArrayList<>());
                    }
                }
                else if (trxMode.equals("monthly")) {
                    if (monthlyLimit >= transactionEo.getTransactionAmount()) {
                        senderCardEo.setBalance(senderCardEo.getBalance() - transactionEo.getTransactionAmount());
                        receiverCardEo.setBalance(receiverCardEo.getBalance() + transactionEo.getTransactionAmount());
                    } else {
                        return new TransactionResponse(false, "Requested amount " +transactionEo.getTransactionAmount()
                                + " is greater than remaining monthly uses amount " + monthlyLimit, new ArrayList<>());
                    }
                }
                else {
                    return new TransactionResponse(false, "Transaction Mode: " + trxMode + " is not available right now.", new ArrayList<>());
                }
            } else {
                return new TransactionResponse(false, "Bad request for transaction. Contact with admin.", new ArrayList<>());
            }

            transactionEo.setTransactionType(TransactionType.getTypefromName(trxType).getValue());
            transactionEo.setTransactionTime(Utils.currentDate());
            repository.save(transactionEo);
            cardRepository.save(senderCardEo);
            cardRepository.save(receiverCardEo);
            LOGGER.info("balance transferred to receiver.");
            return new TransactionResponse(true, "balance transferred to receiver.", getTransactionIntoList(transactionEo));
        }
        return new TransactionResponse(false, "Bad request for transaction....", new ArrayList<>());
    }

    @Transactional
    public TransactionResponse generateRechargeTransaction(com.card.cms.model.Transaction transactionEo, String trxType, String trxMode){
        com.card.cms.model.Card senderCardEo = null;

        if(transactionEo != null) {
            try {
                if (transactionEo.getSenderCard() != null) {
                    senderCardEo = cardRepository.findById(transactionEo.getSenderCard())
                            .orElseThrow(() -> new Exception("No Data found sender card id: " + transactionEo.getSenderCard()));
                }
            } catch (Exception e) {
//            e.printStackTrace();
                return new TransactionResponse(false, e.getMessage(), new ArrayList<>());
            }
            if ( (Utils.isOk(senderCardEo) && senderCardEo.getStatus() == 1) ) {

                if (senderCardEo.getBalance() < transactionEo.getTransactionAmount()) {
                    return new TransactionResponse(false, "Insufficient balance: " +senderCardEo.getBalance(), new ArrayList<>());
                }

                if (senderCardEo.getTransactionLimit().getPerTransactionLimit() < transactionEo.getTransactionAmount()) {
                    return new TransactionResponse(false, "Requested amount " +transactionEo.getTransactionAmount()
                            + " is greater than per transaction uses amount " + senderCardEo.getTransactionLimit().getPerTransactionLimit(), new ArrayList<>());
                }


                Object[] limits = getLimits(senderCardEo);
                Float dailyLimit = (Float) limits[0];
                Float monthlyLimit = (Float) limits[1];


                if (trxMode.equals("daily")) {
                    if (dailyLimit >= transactionEo.getTransactionAmount()) {
                        senderCardEo.setBalance(senderCardEo.getBalance() - transactionEo.getTransactionAmount());
                    } else {
                        return new TransactionResponse(false, "Requested amount " +transactionEo.getTransactionAmount()
                                + " is greater than remaining daily uses amount " + dailyLimit, new ArrayList<>());
                    }
                }
                else if (trxMode.equals("monthly")) {
                    if (monthlyLimit >= transactionEo.getTransactionAmount()) {
                        senderCardEo.setBalance(senderCardEo.getBalance() - transactionEo.getTransactionAmount());
                    } else {
                        return new TransactionResponse(false, "Requested amount " +transactionEo.getTransactionAmount()
                                + " is greater than remaining monthly uses amount " + monthlyLimit, new ArrayList<>());
                    }
                }
            }
            else {
                return new TransactionResponse(false, "Bad request for transaction. Contact with admin.", new ArrayList<>());
            }

            transactionEo.setTransactionType(TransactionType.getTypefromName(trxType).getValue());
            transactionEo.setTransactionTime(Utils.currentDate());
            repository.save(transactionEo);
            cardRepository.save(senderCardEo);
            kafkaService.send("recharge_amount", "recharge_balance", getTransactionIntoList(transactionEo).get(0));
            LOGGER.info("balance transferred to " + transactionEo.getReceiverCard());
            return new TransactionResponse(true, "balance transferred to receiver.", getTransactionIntoList(transactionEo));
        }
        return new TransactionResponse(false, "Bad request for transaction....", new ArrayList<>());
    }

    @Transactional
    public TransactionResponse generateWithdrawTransaction(com.card.cms.model.Transaction transactionEo, String trxType, String trxMode){
        com.card.cms.model.Card senderCardEo = null;

        if(transactionEo != null) {
            try {
                if (transactionEo.getSenderCard() != null) {
                    senderCardEo = cardRepository.findById(transactionEo.getSenderCard())
                            .orElseThrow(() -> new Exception("No Data found sender card id: " + transactionEo.getSenderCard()));
                }
            } catch (Exception e) {
//            e.printStackTrace();
                return new TransactionResponse(false, e.getMessage(), new ArrayList<>());
            }
            if ( (Utils.isOk(senderCardEo) && senderCardEo.getStatus() == 1) ) {

                if (senderCardEo.getBalance() < transactionEo.getTransactionAmount()) {
                    return new TransactionResponse(false, "Insufficient balance: " +senderCardEo.getBalance(), new ArrayList<>());
                }

                if (senderCardEo.getTransactionLimit().getPerTransactionLimit() < transactionEo.getTransactionAmount()) {
                    return new TransactionResponse(false, "Requested amount " +transactionEo.getTransactionAmount()
                            + " is greater than per transaction uses amount " + senderCardEo.getTransactionLimit().getPerTransactionLimit(), new ArrayList<>());
                }

                Object[] limits = getLimits(senderCardEo);
                Float dailyLimit = (Float) limits[0];
                Float monthlyLimit = (Float) limits[1];

                if (trxMode.equals("daily")) {
                    if (dailyLimit >= transactionEo.getTransactionAmount()) {
                        senderCardEo.setBalance(senderCardEo.getBalance() - transactionEo.getTransactionAmount());
                    } else {
                        return new TransactionResponse(false, "Requested amount " +transactionEo.getTransactionAmount()
                                + " cross the remaining daily limit " + dailyLimit, new ArrayList<>());
                    }
                }
                else if (trxMode.equals("monthly")) {
                    if (monthlyLimit >= transactionEo.getTransactionAmount()) {
                        senderCardEo.setBalance(senderCardEo.getBalance() - transactionEo.getTransactionAmount());
                    } else {
                        return new TransactionResponse(false, "Requested amount " +transactionEo.getTransactionAmount()
                                + " cross the remaining monthly limit " + monthlyLimit, new ArrayList<>());
                    }
                }
            }
            else {
                return new TransactionResponse(false, "Bad request for transaction. Contact with admin.", new ArrayList<>());
            }

            transactionEo.setTransactionType(TransactionType.getTypefromName(trxType).getValue());
            transactionEo.setTransactionTime(Utils.currentDate());
            repository.save(transactionEo);
            cardRepository.save(senderCardEo);
            LOGGER.info("balance withdraw from card " + transactionEo.getSenderCard());
            return new TransactionResponse(true, "balance withdraw from card.", getTransactionIntoList(transactionEo));
        }
        return new TransactionResponse(false, "Bad request for transaction....", new ArrayList<>());
    }

    private List<Transaction> getTransactionIntoList (com.card.cms.model.Transaction transaction) {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(transaction));
        return transactions;
    }

    private boolean checkCurrentDate(List<Object[]> customQuery){
        Integer cmp = -2;
        if (customQuery.size() != 0){
            Object[] obj = customQuery.get(0);
            String date = obj[2]+"-"+obj[3]+"-"+obj[4];
            cmp = Utils.currentDate().compareTo(Utils.makeDate(date));

            if (cmp == 0) return true;
        }
        return false;
    }

    private boolean checkCurrentMonth(List<Object[]> customQuery){
        Boolean cmp = false;
        if (customQuery.size() != 0){
            Object[] obj = customQuery.get(0);
            String date = obj[2]+"-"+obj[3]+"-"+obj[4];
            cmp = Utils.currentDate().getMonth() == Utils.makeDate(date).getMonth();
        }
        return cmp;
    }

    private Object[] getLimits(Card senderCardEo){

        Float currentDailyExpense = new Float(0.00);
        List<Object[]> customQuery = repository.getAmountForCardIdByDate(senderCardEo.getId());
        if (customQuery.size() != 0){
            Object[] obj = customQuery.get(0);
            Double tmp = (Double) obj[1];
            currentDailyExpense = tmp.floatValue();
        }
        boolean checkSameDate = checkCurrentDate(customQuery);

        Float dailyLimit = checkSameDate ? (senderCardEo.getTransactionLimit().getDailyLimit()-currentDailyExpense)
                : senderCardEo.getTransactionLimit().getDailyLimit();

        Float currentMonthlyExpense =new Float(0.00);
        List<Object[]> customQueryMonth = repository.getAmountForCardIdByMonth(senderCardEo.getId());
        if (customQuery.size() != 0){
            Object[] obj = customQueryMonth.get(0);
            Double tmp = (Double) obj[1];
            currentMonthlyExpense = tmp.floatValue();
        }
        boolean checkSameMonth = checkCurrentMonth(customQuery);

        Float monthlyLimit = checkSameMonth ? (senderCardEo.getTransactionLimit().getMonthlyLimit()-currentMonthlyExpense)
                : senderCardEo.getTransactionLimit().getMonthlyLimit();

        Object[] objects = new Object[]{dailyLimit,monthlyLimit};
        return objects;
    }
}
