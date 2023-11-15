package mmosii.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import mmosii.bookstore.model.CartItem;
import mmosii.bookstore.repository.shoppingcart.CartItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:database/add-shopping-cart-and-user.sql",
        "classpath:database/add-books-and-categories-to-books-table.sql"})
@Sql(scripts = {"classpath:database/delete-shoppingcarts-and-user.sql",
        "classpath:database/delete-books-and-categories-from-books-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CartItemRepositoryTest {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    @DisplayName("Find shopping cart by valid user id")
    void findByIdAndShoppingCartId_validIds_returnsCartItem() {
        CartItem actual = cartItemRepository
                .findByIdAndShoppingCartId(6L, 3L).get();
        assertThat(actual.getQuantity()).isEqualTo(10);
        assertThat(actual.getBook().getTitle()).isEqualTo("Shantaram");
    }

    @Test
    @DisplayName("Find shopping cart by non existing user id")
    void findByIdAndShoppingCartId_notValidIds_returnsOptionalEmpty() {
        Optional<CartItem> actual = cartItemRepository
                .findByIdAndShoppingCartId(50L, 50L);
        assertThat(actual).isEqualTo(Optional.empty());
    }
}