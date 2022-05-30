package ru.vazh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.vazh.repository.RootRepository;

@SpringBootApplication
public class Spring {
    public static String jsonPath;

    public static void main(String[] args) {

        if (args.length > 1) {
            //https://mkyong.com/logging/logback-set-log-file-name-programmatically/
            System.setProperty("logging.file.name", args[1]);
        }
        SpringApplication.run(Spring.class);
        jsonPath = args[0];
        new RootRepository().initRepo();

    }
}