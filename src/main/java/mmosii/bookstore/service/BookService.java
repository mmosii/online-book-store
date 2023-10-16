package mmosii.bookstore.service;

import java.util.List;
import mmosii.bookstore.dto.BookDto;
import mmosii.bookstore.dto.BookSearchParametersDto;
import mmosii.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    List<BookDto> findAll();

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}
