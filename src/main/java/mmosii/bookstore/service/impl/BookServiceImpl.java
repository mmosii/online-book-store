package mmosii.bookstore.service.impl;

import java.util.List;
import mmosii.bookstore.model.Book;
import mmosii.bookstore.repository.BookRepository;
import mmosii.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);

    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
