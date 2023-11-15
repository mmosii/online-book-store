package mmosii.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mmosii.bookstore.dto.book.BookDto;
import mmosii.bookstore.dto.book.CreateBookRequestDto;
import mmosii.bookstore.exception.EntityNotFoundException;
import mmosii.bookstore.mapper.BookMapper;
import mmosii.bookstore.model.Book;
import mmosii.bookstore.model.Category;
import mmosii.bookstore.repository.book.BookRepository;
import mmosii.bookstore.repository.book.CategoryRepository;
import mmosii.bookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save valid book")
    void save_validRequestDto_returnsValidDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "The Godfather", "Mario Puzo", "553322",
                BigDecimal.valueOf(125.55), null, null, List.of(1L));

        Book book = new Book();
        book.setAuthor(requestDto.author());
        book.setTitle(requestDto.title());

        Category category = new Category();
        category.setId(1L);

        BookDto bookDto = new BookDto();
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());

        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(bookMapper.toBook(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.save(requestDto);

        assertThat(actual).isEqualTo(bookDto);
        verify(bookMapper, times(1)).toBook(requestDto);
        verify(categoryRepository, times(1)).getReferenceById(1L);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(categoryRepository, bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Get book with valid id")
    void getBookById_validId_returnsValidDto() {
        Book book = new Book();
        book.setAuthor("Mario Puzo");
        book.setTitle("The Godfather");
        book.setId(1L);

        BookDto bookDto = new BookDto();
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.getBookById(1L);

        assertThat(actual).isEqualTo(bookDto);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Get book with not existing id")
    void getBookById_notValidId_throwsException() {
        Long id = 100L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(id))
                .isInstanceOf(EntityNotFoundException.class);
        verify(bookRepository, times(1)).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Delete book with valid id")
    void deleteById_validId() {
        Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(id);

        bookService.deleteById(id);

        verify(bookRepository, times(1)).existsById(id);
        verify(bookRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Delete book with not existing id")
    void deleteById_notValidId_throwsException() {
        Long id = 100L;
        when(bookRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteById(id))
                .isInstanceOf(EntityNotFoundException.class);
        verify(bookRepository, times(1)).existsById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Update existing book with valid CreateBookRequestDto")
    void updateBook_validRequestDto_returnsDto() {
        Long id = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "The Godfather", "Mario Puzo", "553322",
                BigDecimal.valueOf(125.55), null, null, List.of(1L));

        Book book = new Book();
        book.setAuthor(requestDto.author());
        book.setTitle(requestDto.title());
        book.setId(id);

        Category category = new Category();
        category.setId(1L);

        BookDto bookDto = new BookDto();
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());

        when(bookRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(bookMapper.toBook(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.update(id, requestDto);

        assertThat(actual).isEqualTo(bookDto);
        verify(bookRepository, times(1)).existsById(id);
        verify(bookMapper, times(1)).toBook(requestDto);
        verify(categoryRepository, times(1)).getReferenceById(1L);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(categoryRepository, bookRepository, bookMapper);
    }
}
