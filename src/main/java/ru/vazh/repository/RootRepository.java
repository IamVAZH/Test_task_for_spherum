package ru.vazh.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.vazh.model.Account;
import ru.vazh.model.App;
import ru.vazh.model.Market;
import ru.vazh.model.Product;
import ru.vazh.util.Cards;
import ru.vazh.util.CustomDeserializer;
import ru.vazh.util.SubscriptionCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.vazh.Spring.jsonPath;

@Repository
public class RootRepository {
    private static App app;
    private static final Logger log = LoggerFactory.getLogger(RootRepository.class);
    private static Cards cards;

    //Initialization of repository for main class
    public void initRepo() {
        try {
            app = new CustomDeserializer().deserialize(jsonPath);
            cards = new Cards(initCards());
        } catch (IOException e) {
            log.error("Can't deserialize your *.json file. Using dataTest.json.");
            e.printStackTrace();
        }
    }

    //Initialization of repository for list of book subscription cards
    private List<SubscriptionCard> initCards() {
        List<SubscriptionCard> cards = new ArrayList<>();
        cards.add(new SubscriptionCard(0, 3000, 5));
        cards.add(new SubscriptionCard(1, 6000, 12));
        cards.add(new SubscriptionCard(2, 60000, 200));
        return cards;
    }

    //Refresh all data necessary for tests
    public void setup() {
        try {
            Product.setCount(0);
            app = new CustomDeserializer().deserialize("dataTest.json");
            cards = new Cards(initCards());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Get account
    public Account accountInfo() {
        return app.getAccount();
    }

    //Get market
    public Market marketInfo() {
        return app.getMarket();
    }

    //Save all changes after deal
    public void saveDeal(Account account, Market market) {
        app.setAccount(account);
        app.setMarket(market);
    }

    //Save all changes after refund
    public void saveAccount(Account account) {
        app.setAccount(account);
    }

    //Get cards
    public Cards subscriptionCards() {
        return cards;
    }
}
