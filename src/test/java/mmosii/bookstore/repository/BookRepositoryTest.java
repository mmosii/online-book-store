package mmosii.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import mmosii.bookstore.model.Book;
import mmosii.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:database/add-categories-input-data.sql",
        "classpath:database/add-books-input-data.sql",
        "classpath:database/add-books-categories-input-data.sql"})
@Sql(scripts = {"classpath:database/delete-books-categories-table.sql",
        "classpath:database/delete-books-table.sql",
        "classpath:database/delete-categories-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books with category that not exists")
    void findAllByCategoryId_NonValidCategory_ReturnsEmptyList() {
        List<Book> actual = bookRepository.findAllByCategoryId(PageRequest.of(0, 10), 100L);
        assertThat(actual).hasSize(0);
    }

    @Test
    @DisplayName("Find all books with existing category")
    void findAllByCategoryId_ValidCategory_ReturnsExpectedBooks() {
        List<Book> actual = bookRepository.findAllByCategoryId(PageRequest.of(0, 10), 2L);
        assertThat(actual).hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("title", "Shantaram");
    }
}
