package mmosii.bookstore.repository;

import java.util.List;
import mmosii.bookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
