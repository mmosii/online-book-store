package mmosii.bookstore.service;

import java.util.List;
import mmosii.bookstore.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
