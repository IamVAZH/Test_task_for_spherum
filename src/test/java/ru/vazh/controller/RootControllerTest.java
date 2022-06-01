package ru.vazh.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.vazh.AccountTestData;
import ru.vazh.MarketTestData;
import ru.vazh.model.Book;
import ru.vazh.repository.RootRepository;
import ru.vazh.service.RootService;

import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RootControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RootRepository rootRepository;

    @Autowired
    private RootService rootService;

    @Before
    public void setUp() {
        rootRepository.setup();
    }

    @Test
    public void accountBooks() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/account"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void products() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/market"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void dealWithNoContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/market/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void dealWithSuccessContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/market/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"amount\":2}"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void dealNotValidId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/market/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":100, \"amount\":2}"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void dealNotEnoughAmount() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/market/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"amount\":30}"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void dealNotEnoughMoney() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/market/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":2, \"amount\":10}"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void productsFilterByStartPrice() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/market")
                        .param("startPrice", String.valueOf(1400)))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void productsFilterByEndPrice() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/market")
                        .param("endPrice", String.valueOf(400)))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void productsFilterByStartEndPrice() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/market")
                        .param("startPrice", String.valueOf(400))
                        .param("endPrice", String.valueOf(1800)))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void productsFilterByName() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/market")
                        .param("name", "Философия Java"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void productsFilterByAuthor() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/market")
                        .param("author", "Брюс Эккель"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void refundWithNoAmount() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/market/refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(MarketTestData.getJsonForRefund(new Book("Совершенный код", "Стив Макконелл"), null))))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void refundSuccessful() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/market/deal")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":0, \"amount\":1}"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/market/refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(MarketTestData.getJsonForRefund(new Book("Совершенный код", "Стив Макконелл"), 1))))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void refundWithError() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/market/refund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(MarketTestData.getJsonForRefund(new Book("Совершенный код", "Стив Макконелл"), 1))))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void subscribeWithError() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/account/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"id\": 5\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void subscribeSuccessful() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/account/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"id\": 1\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void bookmarks() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/account/bookmarks"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void addBookmarksSuccessful() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/account/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "      \"name\" : \"Совершенный код\",\n" +
                                "      \"author\" : \"Стив Макконелл\"\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void addBookmarksError() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/account/bookmarks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "      \"name\" : \"Совершенный код\",\n" +
                        "      \"author\" : \"Стив Макконелл\"\n" +
                        "}"));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/account/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "      \"name\" : \"Совершенный код\",\n" +
                                "      \"author\" : \"Стив Макконелл\"\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void deleteBookmarksError() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/account/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "     \"author\": \"Sierra, Kathy, Bates, Bert\",\n" +
                                "      \"name\": \"Head First Java\"\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void deleteBookmarksSuccessful() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/account/bookmarks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "     \"author\": \"Sierra, Kathy, Bates, Bert\",\n" +
                        "      \"name\": \"Head First Java\"\n" +
                        "}"));

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/account/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "     \"author\": \"Sierra, Kathy, Bates, Bert\",\n" +
                                "      \"name\": \"Head First Java\"\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().is(200));
    }

}