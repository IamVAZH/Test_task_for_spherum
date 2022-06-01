package ru.vazh;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.vazh.model.Account;
import ru.vazh.model.AccountBook;
import ru.vazh.model.Book;
import ru.vazh.model.Product;

import java.util.*;

//Test data for account tests
public class AccountTestData {
    public static final int balance = 20000;
    public static final int addedBooksBalance = 14000;
    public static final int successDealBalance = 10000;
    public static final int subscriptionAmount = 0;

    public static final AccountBook book1 = new AccountBook("Философия Java", "Брюс Эккель", 2);
    public static final AccountBook book2 = new AccountBook("Совершенный код", "Стив Макконелл", 3);

    public static final AccountBook book1DealSuccess = new AccountBook("Философия Java", "Брюс Эккель", 5);
    public static final AccountBook book3DealSuccess = new AccountBook("Effective Java", "Joshua Bloch", 1);

    public static final List<AccountBook> books = List.of();
    public static final List<AccountBook> addedBooks = List.of(book1, book2);
    public static final List<AccountBook> successDealBooks = List.of(book3DealSuccess, book1DealSuccess);

    public static final Book markedBook1 = new Book("Effective Java", "Joshua Bloch");
    public static final Book markedBook2 = new Book("Совершенный код", "Стив Макконелл");

    public static final List<Book> markedAddedBooks = List.of(markedBook1, markedBook2);
    public static final List<Book> markedDeletedBooks = List.of(markedBook2);

    public static String getJson(List<AccountBook> books, int balance, int subscriptionAmount) {
        Map<String, Object> map = new HashMap<>();
        map.put("balance", balance);
        map.put("books", books);
        map.put("subscriptionAmount", subscriptionAmount);
        ObjectMapper obj = new ObjectMapper();

        try {
            return obj.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getJsonForBookmarks(List<Book> books) {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper obj = new ObjectMapper();
        map.put("bookmarks", books);
        try {
            return obj.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
