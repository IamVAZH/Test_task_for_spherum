package ru.vazh.service;

import org.junit.Before;
import org.junit.Test;
import ru.vazh.AccountTestData;
import ru.vazh.MarketTestData;
import ru.vazh.model.Book;
import ru.vazh.repository.RootRepository;

import java.util.List;

import static org.junit.Assert.*;

public class RootServiceTest {

    private final RootRepository rootRepository = new RootRepository();
    private final RootService rootService = new RootService(rootRepository);

    @Before
    public void beforeEach() {
        rootRepository.setup();
    }

    @Test
    public void allBoughtBooks() {
        //no books after serialization
        assertEquals(rootService.allBoughtBooks(), AccountTestData.getJson(AccountTestData.books, AccountTestData.balance, AccountTestData.subscriptionAmount));
    }

    @Test
    public void products() {
        //all valid books in market(without amount=0)
        assertEquals(rootService.products(), MarketTestData.getJson(MarketTestData.products));
    }

    @Test
    public void deal() {
        //amount more than real
        assertFalse(rootService.deal(0, 8));
        //common deal
        assertTrue(rootService.deal(1, 2));
        //id not found
        assertFalse(rootService.deal(50, 10));
        //not enough money
        assertFalse(rootService.deal(2, 10));
    }

    @Test
    public void addNewBooksToAccount() {
        rootService.deal(1, 2);
        rootService.deal(0, 3);
        assertEquals(rootService.allBoughtBooks(), AccountTestData.getJson(AccountTestData.addedBooks, AccountTestData.addedBooksBalance, AccountTestData.subscriptionAmount));
    }

    @Test
    public void markerAfterUnsuccessfulDeal() {
        rootService.deal(100, 0);
        rootService.deal(2, 10);
        assertEquals(rootService.products(), MarketTestData.getJson(MarketTestData.products));
    }

    @Test
    public void marketAfterSuccessfulDeal() {
        rootService.deal(1, 2);
        rootService.deal(0, 3);
        assertEquals(rootService.products(), MarketTestData.getJson(MarketTestData.productsDeal));
    }

    @Test
    public void accountAfterUnsuccessfulDeal() {
        rootService.deal(2, 10);
        assertEquals(rootService.allBoughtBooks(), AccountTestData.getJson(AccountTestData.books, AccountTestData.balance, AccountTestData.subscriptionAmount));
    }


    @Test
    public void accountAfterSuccessfulDeal() {
        rootService.deal(2, 1);
        rootService.deal(1, 5);
        assertEquals(rootService.allBoughtBooks(), AccountTestData.getJson(AccountTestData.successDealBooks, AccountTestData.successDealBalance, AccountTestData.subscriptionAmount));
    }

    @Test
    public void productsFilterByPrice() {
        //only with startPrice
        assertEquals(rootService.productsFilterByPrice(1675, null), MarketTestData.getJson(MarketTestData.productsFilterStartPrice));

        //only with endPrice
        assertEquals(rootService.productsFilterByPrice(null, 1400), MarketTestData.getJson(MarketTestData.productsFilterEndPrice));

        //with startPrice and endPrice
        assertEquals(rootService.productsFilterByPrice(1280, 1990), MarketTestData.getJson(MarketTestData.productsFilterStartEndPrice));

        //no books found
        assertEquals(rootService.productsFilterByPrice(7000, 19000), MarketTestData.getJson(List.of()));
    }

    @Test
    public void productsFilterByName() {
        //find book
        assertEquals(rootService.productsFilterByName("Effective Java"), MarketTestData.getJson(List.of(MarketTestData.product3)));

        //find book but it's amount = 0
        assertEquals(rootService.productsFilterByName("Head First Java"), MarketTestData.getJson(List.of()));

        //no books found
        assertEquals(rootService.productsFilterByName("Чистый код"), MarketTestData.getJson(List.of()));
    }

    @Test
    public void productsFilterByAuthor() {
        //find book
        assertEquals(rootService.productsFilterByAuthor("Joshua Bloch"), MarketTestData.getJson(List.of(MarketTestData.product3)));

        //find book but it's amount = 0
        assertEquals(rootService.productsFilterByAuthor("Sierra, Kathy, Bates, Bert"), MarketTestData.getJson(List.of()));

        //no books found
        assertEquals(rootService.productsFilterByAuthor("Boris Dobrodeyev"), MarketTestData.getJson(List.of()));
    }

    @Test
    public void bookmarks() {
        //marked books at start
        assertEquals(rootService.bookmarks(), AccountTestData.getJsonForBookmarks(List.of()));
    }

    @Test
    public void addBookmarks() {
        //adding new book
        assertTrue(rootService.addBookmarks(new Book("Effective Java", "Joshua Bloch")));
        //adding the same book
        assertFalse(rootService.addBookmarks(new Book("Effective Java", "Joshua Bloch")));
    }

    @Test
    public void deleteBookmarks() {
        //deleting from empty
        assertFalse(rootService.deleteBookmarks(new Book("Совершенный код", "Стив Макконелл")));

        //common deleting
        rootService.addBookmarks(new Book("Совершенный код", "Стив Макконелл"));
        assertTrue(rootService.deleteBookmarks(new Book("Совершенный код", "Стив Макконелл")));
    }


    @Test
    public void bookmarksAfterAddingAndDeletingSomeBooks() {
        rootService.addBookmarks(new Book("Effective Java", "Joshua Bloch"));
        rootService.addBookmarks(new Book("Совершенный код", "Стив Макконелл"));
        //After adding
        assertEquals(rootService.bookmarks(), AccountTestData.getJsonForBookmarks(AccountTestData.markedAddedBooks));

        rootService.deleteBookmarks(new Book("Effective Java", "Joshua Bloch"));
        //After deleting
        assertEquals(rootService.bookmarks(), AccountTestData.getJsonForBookmarks(AccountTestData.markedDeletedBooks));
    }

    @Test
    public void subscribe() {
        //no subscribe card by this id
        assertFalse(rootService.subscribe(100));
        //account balance is not enough
        assertFalse(rootService.subscribe(2));
        //common subscribe on one of cards
        assertTrue(rootService.subscribe(1));
    }

    @Test
    public void refund() {
        //no book found in account books
        assertFalse(rootService.refund(new Book("Effective Java", "Joshua Bloch"), 0));

        //Amount of books on account < amount which we want to refund
        rootService.deal(0, 5);
        assertFalse(rootService.refund(new Book("Совершенный код", "Стив Макконелл"), 10));

        //No books on market, but you still can sell it to market
        rootService.deal(0, 2);
        assertTrue(rootService.refund(new Book("Совершенный код", "Стив Макконелл"), 7));
    }

    @Test
    public void dealWithSubscription() {
        rootService.subscribe(1); // price = 6000, amount = 12 books

        //Not enough amount of books by subscription. You need to buy by you own money. So not enough money
        assertFalse(rootService.deal(1, 15));

        //Price of the book is bigger than 2000. So not enough money
        assertFalse(rootService.deal(2, 10));

        //Buy all books by subscription,max here it's 12. That's why you can buy it
        assertTrue(rootService.deal(1, 12));
    }
}