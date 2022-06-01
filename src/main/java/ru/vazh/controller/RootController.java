package ru.vazh.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.catalina.mapper.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vazh.model.Book;
import ru.vazh.service.RootService;

import javax.servlet.http.HttpServletResponse;

@RestController
public class RootController {
    private final RootService rootService;
    private static final Logger log = LoggerFactory.getLogger(RootController.class);

    public RootController(RootService rootService) {
        this.rootService = rootService;
    }

    //Get all books from account
    @GetMapping(value = "/account")
    @ResponseStatus(HttpStatus.OK)
    public String allBoughtBooks() {
        log.info("All bought books");
        return rootService.allBoughtBooks();
    }

    //Get all books from market
    @GetMapping(value = "/market")
    @ResponseStatus(HttpStatus.OK)
    public String allBooks(@RequestParam(value = "startPrice", required = false) Integer startPrice,
                           @RequestParam(value = "endPrice", required = false) Integer endPrice,
                           @RequestParam(value = "name", required = false) String bookName,
                           @RequestParam(value = "author", required = false) String author) {
        if (startPrice != null || endPrice != null) {
            log.info("All market books filtered by price start with: " + startPrice + " and end with: " + endPrice);
            return rootService.productsFilterByPrice(startPrice, endPrice);
        }
        if (bookName != null) {
            log.info("All market books filtered by name of book: " + bookName);
            return rootService.productsFilterByName(bookName);
        }
        if (author != null) {
            log.info("All market books filtered by author: " + author);
            return rootService.productsFilterByAuthor(author);
        }
        log.info("All market books");
        return rootService.products();
    }

    //Make a deal (buy book(s))
    @PostMapping(value = "/market/deal", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deal(@RequestBody ObjectNode json) {
        int id = json.get("id").asInt();
        int amount = json.get("amount").asInt();
        if (rootService.deal(id, amount)) {
            log.info("Deal successful with id=" + id + " and amount=" + amount);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        log.error("Error during deal with id=" + id + " and amount=" + amount);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    //Refund part of money and sell book(s) to market
    @PostMapping(value = "/market/refund", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refund(@RequestBody ObjectNode json) {
        Book book;
        int amount = json.get("amount").asInt();
        try {
            book = new ObjectMapper().treeToValue(json.get("book"), Book.class);
        } catch (JsonProcessingException e) {
            log.error("Error during deserialization book");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (rootService.refund(book, amount)) {
            log.info("Refund is successful with book: " + book + " and amount: " + amount);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        log.error("Error during refund with book: " + book + " and amount: " + amount);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    //Buy subscription on books
    @PostMapping(value = "/account/subscribe", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> subscribe(@RequestBody ObjectNode json) {
        int id = json.get("id").asInt();

        if (rootService.subscribe(id)) {
            log.info("Successfully subscribed");
            return ResponseEntity.ok(HttpStatus.OK);
        }
        log.error("Error during subscribing");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    //Get all marked books
    @GetMapping(value = "/account/bookmarks")
    @ResponseStatus(HttpStatus.OK)
    public String bookmarks() {
        log.info("List of books with bookmarks");
        return rootService.bookmarks();
    }

    //Add book to marked books
    @PostMapping(value = "/account/bookmarks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBookmarks(@RequestBody Book book) {
        if (rootService.addBookmarks(book)) {
            log.info("Book: " + book + " was successfully added into bookmarks list");
            return ResponseEntity.ok(HttpStatus.OK);
        }
        log.error("Error during adding book: " + book);
        return ResponseEntity.badRequest().body(null);
    }

    //Delete book from marked books
    @DeleteMapping(value = "/account/bookmarks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBookmarks(@RequestBody Book book) {
        if (rootService.deleteBookmarks(book)) {
            log.info("Book: " + book + " was successfully deleted from bookmarks list");
            return ResponseEntity.ok(HttpStatus.OK);
        }
        log.info("Error during deleting book: " + book);
        return ResponseEntity.badRequest().body(null);
    }
}
