package ru.vazh.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class Account {
    private int balance;
    private List<AccountBook> books;

    public Account(@JsonProperty("money") int balance) {
        this.balance = balance;
        this.books = new ArrayList<>();
    }

    public Account(@JsonProperty("money") int balance, @JsonProperty("book") ArrayList<AccountBook> book) {
        this.balance = balance;
        this.books = book;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public List<AccountBook> getBooks() {
        return books;
    }

    public void setBooks(List<AccountBook> books) {
        this.books = books;
    }
}
