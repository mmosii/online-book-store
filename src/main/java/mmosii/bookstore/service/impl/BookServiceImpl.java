package mmosii.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mmosii.bookstore.dto.BookDto;
import mmosii.bookstore.dto.BookSearchParametersDto;
import mmosii.bookstore.dto.CreateBookRequestDto;
import mmosii.bookstore.exception.EntityNotFoundException;
import mmosii.bookstore.mapper.BookMapper;
import mmosii.bookstore.model.Book;
import mmosii.bookstore.repository.book.BookRepository;
import mmosii.bookstore.repository.book.BookSpecificationBuilder;
import mmosii.bookstore.service.BookService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toBook(requestDto)));
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(()
                        -> new EntityNotFoundException("Can't find book by Id " + id));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find book with id " + id);
        }
        Book book = bookMapper.toBook(requestDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find book with id " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParameters) {
        return bookRepository.findAll(bookSpecificationBuilder.build(searchParameters))
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
