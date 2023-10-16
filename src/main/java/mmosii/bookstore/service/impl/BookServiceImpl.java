package mmosii.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mmosii.bookstore.dto.BookDto;
import mmosii.bookstore.dto.CreateBookRequestDto;
import mmosii.bookstore.exception.EntityNotFoundException;
import mmosii.bookstore.mapper.BookMapper;
import mmosii.bookstore.model.Book;
import mmosii.bookstore.repository.BookRepository;
import mmosii.bookstore.service.BookService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.fromDto(requestDto)));
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Can't find book by Id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
