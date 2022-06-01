package ru.vazh.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.vazh.model.*;
import ru.vazh.repository.RootRepository;
import ru.vazh.util.Cards;
import ru.vazh.util.SubscriptionCard;

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

    //Give all account books
    public String allBoughtBooks() {
        Account acc = rootRepository.accountInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("balance", acc.getBalance());
        map.put("subscriptionAmount", acc.getSubscriptionCount());
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

    //Give all books on market
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

    //Convert products into JSON
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

    //Buy book(s) from market
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

        if (product != null && product.getAmount() >= amount &&
                (product.getPrice() <= 2000 && account.getSubscriptionCount() >= amount ||
                        account.getBalance() >= product.getPrice() * amount)) {
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
            if (product.getPrice() <= 2000 && account.getSubscriptionCount() >= amount) {
                account.setSubscriptionCount(account.getSubscriptionCount() - amount);
            } else {
                account.setBalance(account.getBalance() - product.getPrice() * amount);
            }
            account.setBooks(accountBooks);
            product.setAmount(product.getAmount() - amount);
            products.set(id, product);
            market.setProducts(products);
            rootRepository.saveDeal(account, market);
            return true;
        }
        return false;
    }

    //Filter market books by price
    public String productsFilterByPrice(Integer startPrice, Integer endPrice) {
        if (startPrice != null && endPrice != null) {
            return productsToJSON(
                    rootRepository.marketInfo().getProducts().stream()
                            .filter(product -> product.getAmount() > 0 && product.getPrice() >= startPrice && product.getPrice() <= endPrice)
                            .sorted()
                            .collect(Collectors.toList())
            );
        } else if (startPrice != null) {
            return productsToJSON(
                    rootRepository.marketInfo().getProducts().stream()
                            .filter(product -> product.getAmount() > 0 && product.getPrice() >= startPrice)
                            .sorted()
                            .collect(Collectors.toList())
            );
        } else {
            return productsToJSON(
                    rootRepository.marketInfo().getProducts().stream()
                            .filter(product -> product.getAmount() > 0 && product.getPrice() <= endPrice)
                            .sorted()
                            .collect(Collectors.toList())
            );
        }
    }

    //Filter market books by name of the book
    public String productsFilterByName(String bookName) {
        return productsToJSON(
                rootRepository.marketInfo().getProducts().stream()
                        .filter(product -> product.getAmount() > 0 && product.getBook().getName().equals(bookName))
                        .sorted()
                        .collect(Collectors.toList())
        );
    }

    //Filter market books by author of the book
    public String productsFilterByAuthor(String author) {
        return productsToJSON(
                rootRepository.marketInfo().getProducts().stream()
                        .filter(product -> product.getAmount() > 0 && product.getBook().getAuthor().equals(author))
                        .sorted()
                        .collect(Collectors.toList())
        );
    }

    //Refund a book(s) and get 50% money back
    public boolean refund(Book book, int amount) {
        Account account = rootRepository.accountInfo();
        Market market = rootRepository.marketInfo();
        List<AccountBook> books = account.getBooks();
        List<Product> products = market.getProducts();
        AccountBook findBook = account.getBooks().stream()
                .filter(b -> b.getBook().equals(book))
                .findFirst()
                .orElse(null);

        if (findBook != null && findBook.getAmount() >= amount) {
            Product product = market.getProducts().stream()
                    .filter(p -> p.getBook().equals(findBook.getBook()))
                    .findFirst()
                    .orElse(null);
            if (product != null) {
                books.remove(findBook);
                if (findBook.getAmount() - amount != 0) {
                    findBook.setAmount(findBook.getAmount() - amount);
                    books.add(findBook);
                }

                product.setAmount(product.getAmount() + amount);
                account.setBalance((int) (account.getBalance() + product.getPrice() * 0.5 * amount));
                products.set(product.getId(), product);
                market.setProducts(products);
                rootRepository.saveDeal(account, market);
                return true;
            }
        }
        return false;
    }

    //Buy a card to buy with discount future books
    public boolean subscribe(int id) {
        Account account = rootRepository.accountInfo();
        Cards cards = rootRepository.subscriptionCards();
        SubscriptionCard card = cards.getCards().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
        if (card != null && account.getBalance() >= card.getPrice()) {
            account.setBalance(account.getBalance() - card.getPrice());
            account.setSubscriptionCount(account.getSubscriptionCount() + card.getAmount());
            rootRepository.saveAccount(account);
            return true;
        }
        return false;
    }

    //Show the list of marked books
    public String bookmarks() {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper obj = new ObjectMapper();
        List<Book> books = rootRepository.accountInfo().getBookmarks().stream().sorted().collect(Collectors.toList());
        map.put("bookmarks", books);
        try {
            return obj.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Add a book to the list of marked books
    public boolean addBookmarks(Book book) {
        List<Book> books = rootRepository.accountInfo().getBookmarks();

        if (books.contains(book)) {
            return false;
        }
        books.add(book);
        return true;

    }

    //Delete a book from the list of marked books
    public boolean deleteBookmarks(Book book) {
        List<Book> books = rootRepository.accountInfo().getBookmarks();

        if (!books.contains(book)) {
            return false;
        }
        books.remove(book);
        return true;
    }
}
