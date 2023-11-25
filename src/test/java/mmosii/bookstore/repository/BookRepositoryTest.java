package mmosii.bookstore.repository;

import java.util.List;
import mmosii.bookstore.model.Book;
import mmosii.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase
@Sql(scripts = "classpath:database/add-books-and-categories-to-books-table.sql")
@Sql(scripts = "classpath:database/delete-books-and-categories-from-books-table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books with category that not exists")
    void findAllByCategoryId_NonValidCategory_ReturnsEmptyList() {
        List<Book> actual = bookRepository.findAllByCategoryId(PageRequest.of(0, 10), 100L);
        Assertions.assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Find all books with existing category")
    void findAllByCategoryId_ValidCategory_ReturnsExpectedBooks() {
        List<Book> actual = bookRepository.findAllByCategoryId(PageRequest.of(0, 10), 2L);
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("Shantaram", actual.get(0).getTitle());
    }
}
