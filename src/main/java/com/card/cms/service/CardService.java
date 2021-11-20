package com.card.cms.service;

import com.card.cms.payload.Card;
import com.card.cms.payload.CardOpResponse;
import com.card.cms.payload.TransactionLimit;
import com.card.cms.repository.CardRepository;
import com.card.cms.utils.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardService.class);

    @Autowired
    private CardRepository repository;

    @Autowired
    private KafkaService kafkaService;

    public CardOpResponse getCardIntoList() {
        List<com.card.cms.model.Card> cardListEo = repository.findAll();
        if (cardListEo.isEmpty()){
            return new CardOpResponse(false, "No Data found", new ArrayList<>());
        }

        List<Card> cardList = new ArrayList<>();
        for(com.card.cms.model.Card cardEo : cardListEo){
            Card card = new Card(cardEo);
            cardList.add(card);
        }
        return new CardOpResponse(true, "Data found", cardList);
    }

    @Transactional
    public CardOpResponse addNewCard(com.card.cms.model.Card cardEo){
        if(cardEo != null) {
            TransactionLimit transactionLimit = new TransactionLimit(cardEo.getTransactionLimit());
            com.card.cms.model.TransactionLimit transactionLimitEo = new com.card.cms.model.TransactionLimit();
            BeanUtils.copyProperties(transactionLimit, transactionLimitEo);
            transactionLimitEo.setCard(cardEo);
            cardEo.setTransactionLimit(transactionLimitEo);

            repository.save(cardEo);
            return new CardOpResponse(true, "Data added successfully", getCardIntoList(new Card(cardEo)));
        }
        return new CardOpResponse(false, "Failed to add", new ArrayList<>());
    }

    public CardOpResponse getCardInfo(Integer id) {
        try{
            if(id != null && id > 0) {
                com.card.cms.model.Card cardEo = repository.findById(id)
                        .orElseThrow(() -> new Exception("No Data found."));
                CardOpResponse response = new CardOpResponse(true, "Data found", getCardIntoList(new Card(cardEo)));
                return response;
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return new CardOpResponse(false, "No Data found for ID: "+ id, new ArrayList<>());
    }

    @Transactional
    public CardOpResponse updateCardInfo(Integer id, com.card.cms.model.Card card) {
        if (id != null && card != null) {
            com.card.cms.model.Card cardEo = null;

            try{
                cardEo = repository.findById(id)
                        .orElseThrow(() -> new Exception("No Data found."));
            } catch (Exception e) {
//                e.printStackTrace();
                return new CardOpResponse(false, "No Data found for ID: "+ id, new ArrayList<>());
            }
            if (cardEo != null) {
                if(Utils.isOk(card.getPan())){
                    cardEo.setPan(card.getPan());
                }
                if(Utils.isOk(card.getPsn())){
                    cardEo.setPsn(card.getPsn());
                }
                if(Utils.isOk(card.getCardType())){
                    cardEo.setCardType(card.getCardType());
                }
                if(Utils.isOk(card.getExpiryDate())){
                    cardEo.setExpiryDate(card.getExpiryDate());
                }
                if(Utils.isOk(card.getBalance())){
                    cardEo.setBalance(card.getBalance());
                }
                if(Utils.isOk(card.getStatus())){
                    cardEo.setStatus(card.getStatus());
                }

                if(Utils.isOk(card.getTransactionLimit().getPerTransactionLimit())){
                    cardEo.getTransactionLimit().setPerTransactionLimit(card.getTransactionLimit().getPerTransactionLimit());
                }
                if (Utils.isOk(card.getTransactionLimit().getDailyLimit())){
                    cardEo.getTransactionLimit().setDailyLimit(card.getTransactionLimit().getDailyLimit());
                }
                if (Utils.isOk(card.getTransactionLimit().getMonthlyLimit())){
                    cardEo.getTransactionLimit().setMonthlyLimit(card.getTransactionLimit().getMonthlyLimit());
                }
                repository.save(cardEo);
                return new CardOpResponse(true, "Data updated", getCardIntoList(new Card(cardEo)));
            }
        }
        return new CardOpResponse(false, "No Data updated", new ArrayList<>());
    }

    @Transactional
    public void deleteCardById(Integer id) {
        if (id != null) {
            repository.deleteById(id);
        }
    }

    public CardOpResponse updateCardStatusInfo(Integer id, String status) {
        String message = null;
        if (id != null && status != null) {
            com.card.cms.model.Card cardEo = null;
            try{
                cardEo = repository.findById(id)
                        .orElseThrow(() -> new Exception("No Data found."));
            } catch (Exception e) {
//                e.printStackTrace();
                return new CardOpResponse(false, "No Data found for ID: "+ id, new ArrayList<>());
            }

            if( status.equalsIgnoreCase("activate") && cardEo.getStatus() == 0){
                cardEo.setStatus(1);
                cardEo.getExpiryDate().setMonth((cardEo.getExpiryDate().getMonth() - 1 + 3) % 12 + 1);
                message = "Card is activated successfully & sent mail.";
            } else if( status.equalsIgnoreCase("deactivate") && cardEo.getStatus() == 1){
                cardEo.setStatus(0);
                message = "Card is deactivated successfully & sent mail.";
            } else if( (status.equalsIgnoreCase("deactivate") && cardEo.getStatus() == 0) ||
                    (status.equalsIgnoreCase("activate") && cardEo.getStatus() == 1)){
                return new CardOpResponse(false, "Data already updated", new ArrayList<>());
            }

            repository.save(cardEo);
            LOGGER.info("Sending mail to card holder....");
            kafkaService.send("confirmation_mail", "send_mail", message);
            return new CardOpResponse(true, message, getCardIntoList(new Card(cardEo)));
        }
        return new CardOpResponse(false, "Bad Request...", new ArrayList<>());
    }

    private List<Card> getCardIntoList(Card card){
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        return cards;
    }

}
