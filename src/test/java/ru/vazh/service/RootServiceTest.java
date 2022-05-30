package ru.vazh.service;

import org.junit.Before;
import org.junit.Test;
import ru.vazh.AccountTestData;
import ru.vazh.MarketTestData;
import ru.vazh.model.Product;
import ru.vazh.repository.RootRepository;
import ru.vazh.util.CustomDeserializer;

import java.io.IOException;

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
        assertEquals(rootService.allBoughtBooks(), AccountTestData.getJson(AccountTestData.books, AccountTestData.balance));
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
        assertEquals(rootService.allBoughtBooks(), AccountTestData.getJson(AccountTestData.addedBooks, AccountTestData.addedBooksBalance));
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
        assertEquals(rootService.allBoughtBooks(), AccountTestData.getJson(AccountTestData.books, AccountTestData.balance));
    }


    @Test
    public void accountAfterSuccessfulDeal() {
        rootService.deal(2, 1);
        rootService.deal(1, 5);
        assertEquals(rootService.allBoughtBooks(), AccountTestData.getJson(AccountTestData.successDealBooks, AccountTestData.successDealBalance));
    }
}

//class RootServiceTest {
//
//    private RootRepository rootRepository = new RootRepository();
//    private RootService rootService = new RootService(rootRepository);
//
////    @BeforeAll
////    static void beforeAll() {
////
////        Spring appCtx = new Spring();
////        rootRepository = new RootRepository();
////        rootService = new RootService(rootRepository);
////    }
//
//    @Test
//    public void allBoughtBooks() {
//        //no books after serialization
//        assertEquals(rootService.allBoughtBooks(), AccountTestData.getJson(AccountTestData.books));
//    }
//
//    @Test
//    @Ignore
//    public void addSomeBooksToAccount() {
//        rootService.deal(1, 2);
//        rootService.deal(0, 3);
//    }
//
//    @Test
//    public void products() {
//        //all valid books in market(without amount=0)
//        assertEquals(rootService.products(), MarketTestData.getJson());
//    }
//
//    @Test
//    public void deal() {
//        //amount more than real
//        assertFalse(rootService.deal(0, 8));
//        //common deal
//        assertTrue(rootService.deal(1, 2));
//        //id not found
//        assertFalse(rootService.deal(50, 10));
//        //not enough money
//        assertFalse(rootService.deal(2, 10));
//    }
//
//    @Test
//    public void testAllBoughtBooks() {
//    }
//
//    @Test
//    public void testProducts() {
//    }
//
//    @Test
//    public void testDeal() {
//    }
//}