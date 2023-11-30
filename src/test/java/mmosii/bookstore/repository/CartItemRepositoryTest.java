package mmosii.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import mmosii.bookstore.model.Book;
import mmosii.bookstore.model.CartItem;
import mmosii.bookstore.repository.shoppingcart.CartItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase
@Sql(scripts = {"classpath:database/category/add-categories-input-data.sql",
        "classpath:database/book/add-books-input-data.sql",
        "classpath:database/category/add-books-categories-input-data.sql",
        "classpath:database/user/add-users-input-data.sql",
        "classpath:database/shoppingcart/add-shopping-cart-input-data.sql",
        "classpath:database/cartitem/add-cart-items-input-data.sql"})
@Sql(scripts = {"classpath:database/cartitem/delete-cart-items-table.sql",
        "classpath:database/shoppingcart/delete-shopping-cart-table.sql",
        "classpath:database/user/delete-users-table.sql",
        "classpath:database/category/delete-books-categories-table.sql",
        "classpath:database/book/delete-books-table.sql",
        "classpath:database/category/delete-categories-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CartItemRepositoryTest {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @DisplayName("Find shopping cart by valid user id")
    void findByIdAndShoppingCartId_validIds_returnsCartItem() {
        assertThat(cartItemRepository.findByIdAndShoppingCartId(6L, 3L))
                .get()
                .hasFieldOrPropertyWithValue("quantity", 10)
                .extracting(CartItem::getBook)
                .extracting(Book::getTitle)
                .isEqualTo("Shantaram");
    }

    @Test
    @DisplayName("Find shopping cart by non existing user id")
    void findByIdAndShoppingCartId_notValidIds_returnsOptionalEmpty() {
        assertThat(cartItemRepository.findByIdAndShoppingCartId(50L, 50L)).isEmpty();
    }
}
