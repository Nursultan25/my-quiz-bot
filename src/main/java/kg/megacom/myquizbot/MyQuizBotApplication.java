package kg.megacom.myquizbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MyQuizBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyQuizBotApplication.class, args);
    }
}
