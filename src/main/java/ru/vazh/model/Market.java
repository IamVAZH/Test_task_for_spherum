package ru.vazh.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class Market {
    private List<Product> products;

    public Market(@JsonProperty("books") ArrayList<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Market{" +
                "products=" + products +
                '}';
    }
}
