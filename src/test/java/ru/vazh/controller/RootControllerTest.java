package ru.vazh.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.vazh.repository.RootRepository;

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

    @Before
    public void setUp() {
        rootRepository.setup();
    }

    @Test
    public void accountBooksWithNoContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void accountBooksWithContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void productsWithNoContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/market")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void productsWithContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/market")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":4}"))
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
}