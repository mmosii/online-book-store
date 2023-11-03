package mmosii.bookstore.service;

import java.util.List;
import mmosii.bookstore.dto.book.BookDto;
import mmosii.bookstore.dto.book.BookDtoWithoutCategoryIds;
import mmosii.bookstore.dto.book.BookSearchParametersDto;
import mmosii.bookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto getBookById(Long id);

    List<BookDto> findAll(Pageable pageable);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);

    List<BookDtoWithoutCategoryIds> findBooksByCategory(Pageable pageable, Long id);
}
