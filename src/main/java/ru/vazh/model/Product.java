package ru.vazh.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product implements Comparable<Product> {
    private int id;
    private Book book;
    private int price;
    private int amount;
    private static int count;

    @JsonCreator
    public Product(@JsonProperty("author") String author, @JsonProperty("name") String name, @JsonProperty("price") int price, @JsonProperty("amount") int amount) {
        this.id = count++;
        this.book = new Book(name, author);
        this.price = price;
        this.amount = amount;
    }

    public Product(int id, String author, String name, int price, int amount) {
        this.id = id;
        this.book = new Book(name, author);
        this.price = price;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Product.count = count;
    }

    @Override
    public int compareTo(Product o) {
        return Integer.compare(this.getId(), o.getId());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", book=" + book +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}
