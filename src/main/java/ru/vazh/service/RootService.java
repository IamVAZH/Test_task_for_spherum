package ru.vazh.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.vazh.model.Account;
import ru.vazh.model.AccountBook;
import ru.vazh.model.Market;
import ru.vazh.model.Product;
import ru.vazh.repository.RootRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RootService {
    private final RootRepository rootRepository;

    public RootService(RootRepository rootRepository) {
        this.rootRepository = rootRepository;
    }

    public String allBoughtBooks() {
        Account acc = rootRepository.accountInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("balance", acc.getBalance());
        Collections.sort(acc.getBooks());
        map.put("books", acc.getBooks());

        ObjectMapper obj = new ObjectMapper();

        try {
            return obj.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String products() {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper obj = new ObjectMapper();
        List<Product> products = rootRepository.marketInfo().getProducts();
        products = products.stream().
                filter(product -> product.getAmount() > 0)
                .sorted()
                .collect(Collectors.toList());
        return productsToJSON(products);
    }

    public String productsToJSON(List<Product> products) {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper obj = new ObjectMapper();
        map.put("products", products);
        try {
            return obj.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deal(int id, int amount) {
        Account account = rootRepository.accountInfo();
        Market market = rootRepository.marketInfo();
        List<Product> products = rootRepository.marketInfo().getProducts();
        Collections.sort(products);
        Product product;

        if (id >= 0 && id < products.size()) {
            product = products.get(id);
        } else {
            product = null;
        }

        if (product != null && product.getAmount() >= amount && account.getBalance() >= product.getPrice() * amount) {
            List<AccountBook> accountBooks = account.getBooks();
            AccountBook accountBook = accountBooks.stream()
                    .filter(book -> book.getBook().equals(product.getBook()))
                    .findFirst()
                    .orElse(null);
            if (accountBook != null) {
                accountBooks.remove(accountBook);
                accountBook.setAmount(accountBook.getAmount() + amount);
                accountBooks.add(accountBook);
            } else {
                accountBooks.add(new AccountBook(product.getBook(), amount));
            }
            Collections.sort(accountBooks);
            account.setBalance(account.getBalance() - product.getPrice() * amount);
            account.setBooks(accountBooks);
            product.setAmount(product.getAmount() - amount);
            products.set(id, product);
            market.setProducts(products);
            rootRepository.saveDeal(account, market);
            return true;
        }
        return false;
    }
}
