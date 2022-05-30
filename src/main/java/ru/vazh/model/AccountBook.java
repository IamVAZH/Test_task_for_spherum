package ru.vazh.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountBook implements Comparable<AccountBook> {
    private Book book;
    private int amount;

    @JsonCreator
    public AccountBook(@JsonProperty("book") Book book, @JsonProperty("amount") int amount) {
        this.book = book;
        this.amount = amount;
    }

    public AccountBook(String name, String author, int amount) {
        this.book = new Book(name, author);
        this.amount = amount;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(AccountBook o) {
        return Integer.compare(this.getBook().hashCode(), o.getBook().hashCode());
    }
}
