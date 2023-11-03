package mmosii.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import mmosii.bookstore.dto.shoppingcart.CartItemDto;
import mmosii.bookstore.dto.shoppingcart.CartItemRequestDto;
import mmosii.bookstore.dto.shoppingcart.CartItemUpdateDto;
import mmosii.bookstore.dto.shoppingcart.ShoppingCartDto;
import mmosii.bookstore.exception.EntityNotFoundException;
import mmosii.bookstore.mapper.CartItemMapper;
import mmosii.bookstore.mapper.ShoppingCartMapper;
import mmosii.bookstore.model.CartItem;
import mmosii.bookstore.model.ShoppingCart;
import mmosii.bookstore.model.User;
import mmosii.bookstore.repository.book.BookRepository;
import mmosii.bookstore.repository.shoppingcart.CartItemRepository;
import mmosii.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mmosii.bookstore.repository.user.UserRepository;
import mmosii.bookstore.service.ShoppingCartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartMapper.toDto(getUserCart());
    }

    @Override
    public CartItemDto addCartItem(CartItemRequestDto requestDto) {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(requestDto.quantity());
        cartItem.setBook(bookRepository.findById(requestDto.bookId()).orElseThrow(()
                -> new EntityNotFoundException("Can't find book with id: " + requestDto.bookId())));
        cartItem.setShoppingCart(getUserCart());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDto updateCartItem(Long itemId, CartItemUpdateDto updateDto) {
        ShoppingCart shoppingCart = getUserCart();
        CartItem cartItem = findCartItem(
                itemId, shoppingCart.getId(), shoppingCart.getUser().getId());
        cartItem.setQuantity(updateDto.quantity());
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void removeCartItem(Long itemId) {
        ShoppingCart shoppingCart = getUserCart();
        CartItem cartItem = findCartItem(
                itemId, shoppingCart.getId(), shoppingCart.getUser().getId());
        cartItemRepository.delete(cartItem);
    }

    private ShoppingCart getUserCart() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new EntityNotFoundException("Can't find user by email"));
        return shoppingCartRepository.findByUserId(user.getId()).orElseGet(() -> {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUser(user);
            return shoppingCartRepository.save(newCart);
        });
    }

    private CartItem findCartItem(Long itemId, Long shoppingCartId, Long userId) {
        return cartItemRepository.findByIdAndShoppingCartId(
                itemId, shoppingCartId).orElseThrow(()
                        -> new EntityNotFoundException(
                "Cant find cart item by id: %s for user id: %s"
                        .formatted(itemId, userId)));
    }
}
