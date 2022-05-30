package ru.vazh;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.vazh.model.Product;

import java.util.*;

public class MarketTestData {
    public static final Product product1 = new Product(0, "Стив Макконелл", "Совершенный код", 1000, 7);
    public static final Product product2 = new Product(1, "Брюс Эккель", "Философия Java", 1500, 15);
    public static final Product product3 = new Product(2, "Joshua Bloch", "Effective Java", 2500, 10);

    public static final Product product1Deal = new Product(0, "Стив Макконелл", "Совершенный код", 1000, 4);
    public static final Product product2Deal = new Product(1, "Брюс Эккель", "Философия Java", 1500, 13);

    public static final List<Product> products = List.of(product1, product2, product3);
    public static final List<Product> productsDeal = List.of(product1Deal, product2Deal, product3);

    public static String getJson(List<Product> products) {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper obj = new ObjectMapper();
        map.put("products", products);
        try {
            return obj.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
