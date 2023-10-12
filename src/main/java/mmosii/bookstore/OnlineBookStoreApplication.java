package mmosii.bookstore;

import java.math.BigDecimal;
import mmosii.bookstore.model.Book;
import mmosii.bookstore.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService bookService) {
        return args -> {
            Book firstBook = new Book();
            firstBook.setAuthor("Mario Puzo");
            firstBook.setTitle("The Godfather");
            firstBook.setPrice(BigDecimal.valueOf(556));
            firstBook.setIsbn("ISBN 0-545-01022-5");
            Book secondBook = new Book();
            secondBook.setAuthor("Mario Puzo");
            secondBook.setTitle("The Sicilian");
            secondBook.setPrice(BigDecimal.valueOf(265));
            secondBook.setIsbn("ISBN 0-565-01035-1");
            bookService.save(firstBook);
            bookService.save(secondBook);
            System.out.println(bookService.findAll());
        };
    }
}
