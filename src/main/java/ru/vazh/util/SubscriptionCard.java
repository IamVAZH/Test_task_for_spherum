package ru.vazh.util;

public class SubscriptionCard {
    private int id;
    private int price;
    private int amount;

    public SubscriptionCard(int id, int price, int amount) {
        this.id = id;
        this.price = price;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
