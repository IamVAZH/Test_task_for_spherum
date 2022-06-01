package ru.vazh.util;

import java.util.List;

public class Cards {
    private List<SubscriptionCard> cards;

    public Cards(List<SubscriptionCard> cards) {
        this.cards = cards;
    }

    public List<SubscriptionCard> getCards() {
        return cards;
    }

    public void setCards(List<SubscriptionCard> cards) {
        this.cards = cards;
    }
}
