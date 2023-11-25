package mmosii.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import mmosii.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase
@Sql(scripts = {"classpath:database/add-shopping-cart-and-user.sql",
        "classpath:database/add-books-and-categories-to-books-table.sql"})
@Sql(scripts = {"classpath:database/delete-shoppingcarts-and-user.sql",
        "classpath:database/delete-books-and-categories-from-books-table.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find shopping cart by valid user id")
    void findByUserId_validId_returnsShoppingCart() {
        assertThat(shoppingCartRepository.findByUserId(5L))
                .get()
                .hasFieldOrPropertyWithValue("id", 3L);
    }

    @Test
    @DisplayName("Find shopping cart by non existing user id")
    void findByUserId_notValidId_returnsOptionalEmpty() {
        assertThat(shoppingCartRepository.findByUserId(50L)).isEmpty();
    }
}
