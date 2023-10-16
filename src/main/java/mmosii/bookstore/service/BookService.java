package mmosii.bookstore.service;

import java.util.List;
import mmosii.bookstore.dto.BookDto;
import mmosii.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    List<BookDto> findAll();
}
