package ru.vazh;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.vazh.model.Account;
import ru.vazh.model.AccountBook;
import ru.vazh.model.Product;

import java.util.*;

public class AccountTestData {
    public static final int balance = 20000;
    public static final int addedBooksBalance = 14000;
    public static final int successDealBalance = 10000;

    //    private static final AccountBook book3 = new AccountBook("Soft Skills: The Software developer's life manual", "John Sonmez", 8);
    public static final AccountBook book1 = new AccountBook("Философия Java", "Брюс Эккель", 2);
    public static final AccountBook book2 = new AccountBook("Совершенный код", "Стив Макконелл", 3);

    public static final AccountBook book1DealSuccess = new AccountBook("Философия Java", "Брюс Эккель", 5);
    public static final AccountBook book3DealSuccess = new AccountBook("Effective Java", "Joshua Bloch", 1);

    public static final List<AccountBook> books = List.of();
    public static final List<AccountBook> addedBooks = List.of(book1, book2);
    public static final List<AccountBook> successDealBooks = List.of(book3DealSuccess, book1DealSuccess);

    public static String getJson(List<AccountBook> books, int balance) {
        Map<String, Object> map = new HashMap<>();
        map.put("balance", balance);
        map.put("books", books);
        ObjectMapper obj = new ObjectMapper();

        try {
            return obj.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
