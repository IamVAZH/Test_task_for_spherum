package ru.vazh.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class Account {
    private int balance;
    private List<AccountBook> books;
    private int subscriptionCount;
    private List<Book> bookmarks;

    public Account(@JsonProperty("money") int balance) {
        this.balance = balance;
        this.books = new ArrayList<>();
        this.bookmarks = new ArrayList<>();
        this.subscriptionCount = 0;
    }

    public Account(@JsonProperty("money") int balance, @JsonProperty("book") ArrayList<AccountBook> book) {
        this.balance = balance;
        this.books = book;
        this.subscriptionCount = 0;
        this.bookmarks = new ArrayList<>();
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

    public int getSubscriptionCount() {
        return subscriptionCount;
    }

    public void setSubscriptionCount(int subscriptionCount) {
        this.subscriptionCount = subscriptionCount;
    }

    public List<Book> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Book> bookmarks) {
        this.bookmarks = bookmarks;
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", books=" + books +
                ", subscriptionCount=" + subscriptionCount +
                ", bookmarks=" + bookmarks +
                '}';
    }
}
