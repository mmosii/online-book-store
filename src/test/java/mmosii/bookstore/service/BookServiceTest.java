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
import java.util.Set;
import mmosii.bookstore.dto.book.BookDto;
import mmosii.bookstore.dto.book.CreateBookRequestDto;
import mmosii.bookstore.exception.EntityNotFoundException;
import mmosii.bookstore.mapper.BookMapper;
import mmosii.bookstore.mapper.impl.BookMapperImpl;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Spy
    private BookMapper bookMapper = new BookMapperImpl();
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save valid book")
    void save_validRequestDto_returnsValidDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "The Godfather", "Mario Puzo", "553322",
                BigDecimal.valueOf(125.55), "some desc", "some url", List.of(1L));

        Category category = new Category();
        category.setId(1L);

        Book book = new Book();
        book.setAuthor(requestDto.author());
        book.setTitle(requestDto.title());
        book.setIsbn(requestDto.isbn());
        book.setPrice(requestDto.price());
        book.setDescription(requestDto.description());
        book.setCoverImage(requestDto.coverImage());
        book.setCategories(Set.of(category));

        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(bookRepository.save(book)).thenReturn(book);

        BookDto actual = bookService.save(requestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("title", requestDto.title())
                .hasFieldOrPropertyWithValue("author", requestDto.author())
                .hasFieldOrPropertyWithValue("price", requestDto.price())
                .hasFieldOrPropertyWithValue("description", requestDto.description())
                .hasFieldOrPropertyWithValue("coverImage", requestDto.coverImage())
                .hasFieldOrPropertyWithValue("categoryIds", requestDto.categoryIds());
    }

    @Test
    @DisplayName("Get book with valid id")
    void getBookById_validId_returnsValidDto() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("Mario Puzo");
        book.setTitle("The Godfather");
        book.setIsbn("553322");
        book.setPrice(BigDecimal.valueOf(125.55));
        book.setDescription("some desc");
        book.setCoverImage("some url");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto actual = bookService.getBookById(1L);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", book.getId())
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrPropertyWithValue("author", book.getAuthor())
                .hasFieldOrPropertyWithValue("price", book.getPrice())
                .hasFieldOrPropertyWithValue("description", book.getDescription())
                .hasFieldOrPropertyWithValue("coverImage", book.getCoverImage());
    }

    @Test
    @DisplayName("Get book with not existing id")
    void getBookById_notValidId_throwsException() {
        Long id = 100L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(id))
                .isInstanceOf(EntityNotFoundException.class);
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
    }

    @Test
    @DisplayName("Update existing book with valid CreateBookRequestDto")
    void updateBook_validRequestDto_returnsDto() {
        Long id = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "The Godfather", "Mario Puzo", "553322",
                BigDecimal.valueOf(125.55), "some desc", "some url", List.of(id));

        Category category = new Category();
        category.setId(id);

        Book book = new Book();
        book.setId(id);
        book.setAuthor(requestDto.author());
        book.setTitle(requestDto.title());
        book.setIsbn(requestDto.isbn());
        book.setPrice(requestDto.price());
        book.setDescription(requestDto.description());
        book.setCoverImage(requestDto.coverImage());
        book.setCategories(Set.of(category));

        when(bookRepository.existsById(id)).thenReturn(true);
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(bookRepository.save(book)).thenReturn(book);

        BookDto actual = bookService.update(id, requestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("title", requestDto.title())
                .hasFieldOrPropertyWithValue("author", requestDto.author())
                .hasFieldOrPropertyWithValue("price", requestDto.price())
                .hasFieldOrPropertyWithValue("description", requestDto.description())
                .hasFieldOrPropertyWithValue("coverImage", requestDto.coverImage())
                .hasFieldOrPropertyWithValue("categoryIds", requestDto.categoryIds());
    }
}
