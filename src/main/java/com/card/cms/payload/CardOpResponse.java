package com.card.cms.payload;

import com.card.cms.utils.payload.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CardOpResponse extends ApiResponse {
    private List<Card> cardList;

    public CardOpResponse(boolean success, String message, List<Card> cards){
        super(success, message);
        this.cardList = cards;
    }
}
