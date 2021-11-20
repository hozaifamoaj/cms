package com.card.cms.controller;

import com.card.cms.payload.CardOpResponse;
import com.card.cms.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService service;

    @PostMapping("/card")
    public ResponseEntity create(@Valid @RequestBody com.card.cms.model.Card card) {
        CardOpResponse response = service.addNewCard(card);
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getCardList().get(0));
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    @GetMapping("/card")
    public ResponseEntity findAllCards() {
        CardOpResponse response = service.getCardIntoList();
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getCardList());
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    @GetMapping("/card/{id}")
    public ResponseEntity findOne(@PathVariable(value = "id") Integer id) {
        CardOpResponse response = service.getCardInfo(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getCardList().get(0));
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    @PutMapping("/card/{id}")
    public ResponseEntity update (@PathVariable(value = "id") Integer cardId,
                          @Valid @RequestBody com.card.cms.model.Card cardEo){
        CardOpResponse response = service.updateCardInfo(cardId, cardEo);
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getCardList().get(0));
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    @DeleteMapping("/card/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") Integer cardId) {
        service.deleteCardById(cardId);
        return ResponseEntity.ok().body("ID: " + cardId + " is deleted");
    }

    @PostMapping("/card/{id}/activate")
    public ResponseEntity updateCardStatusActive(@PathVariable(value = "id") Integer cardId){
        CardOpResponse response = service.updateCardStatusInfo(cardId, "activate");
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getMessage());
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }

    @PostMapping("/card/{id}/deactivate")
    public ResponseEntity updateCardStatusDeactive(@PathVariable(value = "id") Integer cardId){
        CardOpResponse response = service.updateCardStatusInfo(cardId, "deactivate");
        if (response.isSuccess()) {
            return ResponseEntity.ok().body(response.getMessage());
        }
        return ResponseEntity.badRequest().body(response.getMessage());
    }
}
