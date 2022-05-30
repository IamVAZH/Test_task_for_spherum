package ru.vazh.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class App {
    private Account account;
    private Market market;

    @JsonCreator
    public App(@JsonProperty("account") Account account, @JsonProperty("books") Market market) {
        this.account = account;
        this.market = market;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }
}
