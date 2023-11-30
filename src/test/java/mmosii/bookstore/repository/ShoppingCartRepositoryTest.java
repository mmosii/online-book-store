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
