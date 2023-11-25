package mmosii.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mmosii.bookstore.dto.shoppingcart.CartItemDto;
import mmosii.bookstore.dto.shoppingcart.CartItemRequestDto;
import mmosii.bookstore.dto.shoppingcart.CartItemUpdateDto;
import mmosii.bookstore.dto.shoppingcart.ShoppingCartDto;
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
@Sql(scripts = {"classpath:database/add-shopping-cart-and-user.sql",
        "classpath:database/add-books-and-categories-to-books-table.sql"})
@Sql(scripts = {"classpath:database/delete-shoppingcarts-and-user.sql",
        "classpath:database/delete-books-and-categories-from-books-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ShoppingCartControllerTest {
    private static MockMvc mockMvc;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Get shopping cart for valid user")
    @WithMockUser(username = "test@gmail.com", authorities = {"USER", "ADMIN"})
    void getShoppingCart_validUser_returnsDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartDto.class);
        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("userId", 5L)
                .hasFieldOrProperty("cartItems").isNotNull();
    }

    @Test
    @DisplayName("Add valid cartItem to shopping cart")
    @WithMockUser(username = "test@gmail.com", authorities = {"USER", "ADMIN"})
    void addBookToCart_validRequestDto_returnsDto() throws Exception {
        CartItemRequestDto requestDto = new CartItemRequestDto(2L, 10);
        CartItemDto expected = new CartItemDto(null, 2L, "Shantaram", 10);

        MvcResult result = mockMvc.perform(post("/api/cart")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CartItemDto.class);
        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("bookId", expected.bookId())
                .hasFieldOrPropertyWithValue("bookTitle", expected.bookTitle())
                .hasFieldOrPropertyWithValue("quantity", expected.quantity());
    }

    @Test
    @DisplayName("Update existing cartItem")
    @WithMockUser(username = "test@gmail.com", authorities = {"USER", "ADMIN"})
    void updateBookQuantity_validUpdateDto_returnsDto() throws Exception {
        Long id = 6L;
        CartItemUpdateDto updateDto = new CartItemUpdateDto(20);
        CartItemDto expected = new CartItemDto(6L, 2L, "Shantaram", 20);

        MvcResult result = mockMvc.perform(put("/api/cart/cart-items/" + id)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CartItemDto.class);
        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("bookId", expected.bookId())
                .hasFieldOrPropertyWithValue("bookTitle", expected.bookTitle())
                .hasFieldOrPropertyWithValue("quantity", expected.quantity());
    }

    @Test
    @DisplayName("Delete cartItem from shopping cart")
    @WithMockUser(username = "test@gmail.com", authorities = {"USER", "ADMIN"})
    void deleteBookFromCart_validId() throws Exception {
        Long id = 6L;

        mockMvc.perform(delete("/api/cart/cart-items/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(bookRepository.findById(id)).isEmpty();
    }
}
