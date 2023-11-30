package mmosii.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import mmosii.bookstore.dto.book.BookDto;
import mmosii.bookstore.dto.book.CreateBookRequestDto;
import mmosii.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:database/add-categories-input-data.sql",
        "classpath:database/add-books-input-data.sql",
        "classpath:database/add-books-categories-input-data.sql"})
@Sql(scripts = {"classpath:database/delete-books-categories-table.sql",
        "classpath:database/delete-books-table.sql",
        "classpath:database/delete-categories-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Create book with valid dto")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void createBook_validRequestDto_returnsDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "The Godfather", "Mario Puzo", "553322",
                BigDecimal.valueOf(125.55), "some desc", "some img", List.of(1L));

        MvcResult result = mockMvc.perform(post("/api/books")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertThat(actual)
                .hasFieldOrPropertyWithValue("title", requestDto.title())
                .hasFieldOrPropertyWithValue("author", requestDto.author())
                .hasFieldOrPropertyWithValue("price", requestDto.price())
                .hasFieldOrPropertyWithValue("description", requestDto.description())
                .hasFieldOrPropertyWithValue("coverImage", requestDto.coverImage())
                .hasFieldOrPropertyWithValue("categoryIds", requestDto.categoryIds())
                .hasFieldOrPropertyWithValue("id", 3L);
    }

    @Test
    @DisplayName("Get book by valid id")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void getBookById_validId_returnsDto() throws Exception {
        Long id = 1L;

        BookDto expected = new BookDto();
        expected.setTitle("The Godfather");
        expected.setPrice(BigDecimal.valueOf(350.55));

        MvcResult result = mockMvc.perform(get("/api/books/" + id))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("title", expected.getTitle())
                .hasFieldOrPropertyWithValue("price", expected.getPrice())
                .hasFieldOrPropertyWithValue("id", id);
    }

    @Test
    @DisplayName("Delete book by valid id")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void deleteBookById_validId() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/books/" + id))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(bookRepository.findById(id)).isEmpty();
    }
}
