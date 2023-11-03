package mmosii.bookstore.service;

import mmosii.bookstore.dto.shoppingcart.CartItemDto;
import mmosii.bookstore.dto.shoppingcart.CartItemRequestDto;
import mmosii.bookstore.dto.shoppingcart.CartItemUpdateDto;
import mmosii.bookstore.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    CartItemDto addCartItem(CartItemRequestDto requestDto);

    CartItemDto updateCartItem(Long itemId, CartItemUpdateDto updateDto);

    void removeCartItem(Long itemId);
}
