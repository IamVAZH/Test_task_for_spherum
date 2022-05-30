package ru.vazh.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.vazh.model.Account;
import ru.vazh.model.App;
import ru.vazh.model.Market;
import ru.vazh.model.Product;
import ru.vazh.util.CustomDeserializer;

import java.io.IOException;
import java.util.ArrayList;

import static ru.vazh.Spring.jsonPath;

@Repository
public class RootRepository {
    private static App app;
    private static final Logger log = LoggerFactory.getLogger(RootRepository.class);

    public void initRepo() {
        try {
            app = new CustomDeserializer().deserialize(jsonPath);
        } catch (IOException e) {
            log.error("Can't deserialize your *.json file. Using dataTest.json.");
            e.printStackTrace();
        }
    }

    public void setup() {
        try {
            Product.setCount(0);
            app = new CustomDeserializer().deserialize("dataTest.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Account accountInfo() {
        return app.getAccount();
    }

    public Market marketInfo() {
        return app.getMarket();
    }

    public void saveDeal(Account account, Market market) {
        app.setAccount(account);
        app.setMarket(market);
    }
}
