package mmosii.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mmosii.bookstore.dto.shoppingcart.CartItemDto;
import mmosii.bookstore.dto.shoppingcart.CartItemRequestDto;
import mmosii.bookstore.dto.shoppingcart.CartItemUpdateDto;
import mmosii.bookstore.dto.shoppingcart.ShoppingCartDto;
import mmosii.bookstore.mapper.CartItemMapper;
import mmosii.bookstore.mapper.ShoppingCartMapper;
import mmosii.bookstore.model.Book;
import mmosii.bookstore.model.CartItem;
import mmosii.bookstore.model.ShoppingCart;
import mmosii.bookstore.model.User;
import mmosii.bookstore.repository.book.BookRepository;
import mmosii.bookstore.repository.shoppingcart.CartItemRepository;
import mmosii.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mmosii.bookstore.repository.user.UserRepository;
import mmosii.bookstore.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Get already existing shopping cart")
    void getShoppingCart_existingCart_returnsCartDto() {
        User user = new User();
        user.setId(5L);
        user.setEmail("test@gmail.com");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(3L);

        ShoppingCartDto expected = new ShoppingCartDto(shoppingCart.getId(),
                shoppingCart.getUser().getId(), null);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@gmail.com");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Get new shopping cart")
    void getShoppingCart_newCart_returnsCartDto() {
        User user = new User();
        user.setId(5L);
        user.setEmail("test@gmail.com");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        ShoppingCartDto expected = new ShoppingCartDto(shoppingCart.getId(),
                shoppingCart.getUser().getId(), null);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@gmail.com");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(any())).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart();

        assertThat(actual.userId()).isEqualTo(expected.userId());
    }

    @Test
    @DisplayName("Add valid cart item to shopping cart")
    void addCartItem_validRequestDto_returnsCartItemDto() {
        Book book = new Book();
        book.setTitle("The Godfather");
        book.setId(3L);

        User user = new User();
        user.setId(5L);
        user.setEmail("test@gmail.com");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        CartItemRequestDto requestDto = new CartItemRequestDto(3L, 5);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(requestDto.quantity());
        cartItem.setShoppingCart(shoppingCart);

        CartItemDto expected = new CartItemDto(1L, book.getId(),
                book.getTitle(), requestDto.quantity());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@gmail.com");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.save(any())).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(expected);

        CartItemDto actual = shoppingCartService.addCartItem(requestDto);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Update existing cart item in shopping cart")
    void updateCartItem_validUpdateDto_returnsCartItemDto() {
        User user = new User();
        user.setId(5L);
        user.setEmail("test@gmail.com");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(5);

        CartItemUpdateDto updateDto = new CartItemUpdateDto(50);

        CartItemDto expected = new CartItemDto(null,
                null, null, updateDto.quantity());
        Long itemId = 1L;

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@gmail.com");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findByIdAndShoppingCartId(itemId,
                shoppingCart.getId())).thenReturn(Optional.of(cartItem));
        when(cartItemMapper.toDto(cartItem)).thenReturn(expected);

        CartItemDto actual = shoppingCartService.updateCartItem(1L, updateDto);

        assertThat(actual.quantity()).isEqualTo(expected.quantity());
    }

    @Test
    @DisplayName("Removes existing cart item from shopping cart")
    void removeCartItem_validId() {
        User user = new User();
        user.setId(5L);
        user.setEmail("test@gmail.com");

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(5);

        Long itemId = 1L;

        when(cartItemRepository.findByIdAndShoppingCartId(itemId,
                shoppingCart.getId())).thenReturn(Optional.of(cartItem));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@gmail.com");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        doNothing().when(cartItemRepository).delete(cartItem);

        shoppingCartService.removeCartItem(1L);

        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByEmail("test@gmail.com");
        verify(shoppingCartRepository, times(1)).findByUserId(user.getId());
        verify(cartItemRepository, times(1))
                .findByIdAndShoppingCartId(itemId, shoppingCart.getId());
        verify(cartItemRepository, times(1)).delete(cartItem);
        verifyNoMoreInteractions(securityContext, authentication,
                userRepository, shoppingCartRepository, cartItemMapper);
    }
}
