package ru.vazh.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String allBoughtBooks() {
        log.info("All bought books");
        return rootService.allBoughtBooks();
    }

    //Get all books from market
    @GetMapping(value = "/market", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String allBooks() {
        log.info("All market books");
        return rootService.products();
    }

    //Make a deal (buy book(s))
    @PostMapping(value = "/market/deal", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deal(@RequestBody ObjectNode json, HttpServletResponse response) {
        int id = json.get("id").asInt();
        int amount = json.get("amount").asInt();
        if (rootService.deal(id, amount)) {
            log.info("Deal successful with id=" + id + " and amount=" + amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Error during deal with id=" + id + " and amount=" + amount);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
