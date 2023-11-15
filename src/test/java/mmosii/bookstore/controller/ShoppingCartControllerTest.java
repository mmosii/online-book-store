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
    protected static MockMvc mockMvc;

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
        ShoppingCartDto expected = new ShoppingCartDto(3L, 5L, null);

        MvcResult result = mockMvc.perform(get("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartDto.class);
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(expected.id());
        assertThat(actual.userId()).isEqualTo(expected.userId());
    }

    @Test
    @DisplayName("Add valid cartItem to shopping cart")
    @WithMockUser(username = "test@gmail.com", authorities = {"USER", "ADMIN"})
    void addBookToCart_validRequestDto_returnsDto() throws Exception {
        CartItemRequestDto requestDto = new CartItemRequestDto(2L, 10);
        CartItemDto expected = new CartItemDto(null, 2L, "Shantaram", 10);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/api/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CartItemDto.class);
        assertThat(actual).isNotNull();
        assertThat(actual.bookId()).isEqualTo(expected.bookId());
        assertThat(actual.bookTitle()).isEqualTo(expected.bookTitle());
        assertThat(actual.quantity()).isEqualTo(expected.quantity());
    }

    @Test
    @DisplayName("Update existing cartItem")
    @WithMockUser(username = "test@gmail.com", authorities = {"USER", "ADMIN"})
    void updateBookQuantity_validUpdateDto_returnsDto() throws Exception {
        Long id = 6L;
        CartItemUpdateDto updateDto = new CartItemUpdateDto(20);
        CartItemDto expected = new CartItemDto(6L, 2L, "Shantaram", 20);

        String jsonRequest = objectMapper.writeValueAsString(updateDto);
        MvcResult result = mockMvc.perform(put("/api/cart/cart-items/" + id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CartItemDto.class);
        assertThat(actual).isNotNull();
        assertThat(actual.bookId()).isEqualTo(expected.bookId());
        assertThat(actual.bookTitle()).isEqualTo(expected.bookTitle());
        assertThat(actual.quantity()).isEqualTo(expected.quantity());
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
    }
}
