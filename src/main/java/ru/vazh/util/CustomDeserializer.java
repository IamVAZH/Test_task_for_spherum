package ru.vazh.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.core.io.ClassPathResource;
import ru.vazh.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CustomDeserializer {

    public App deserialize(String filePath) throws IOException {
        Account account;
        Market market;
        String json;

        ObjectMapper mapper = new ObjectMapper();

        //access to file into jar
        //https://www.baeldung.com/spring-classpath-file-access (4.2)
        InputStream resource = new ClassPathResource(filePath).getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
            json = reader.lines().collect(Collectors.joining("\n"));
        }

        JsonNode node = mapper.readTree(json);
        JsonNode nodeAccount = node.get("account");
        JsonNode nodeMarket = node.get("books");
        account = new Account((Integer) (nodeAccount.get("money").numberValue()));

        ArrayNode arrayNode = (ArrayNode) nodeMarket;
        market = new Market(mapper.readValue(arrayNode.toString(), new TypeReference<ArrayList<Product>>() {
        }));

        return new App(account, market);
    }
}
