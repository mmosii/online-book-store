package mmosii.bookstore.mapper;

import mmosii.bookstore.dto.shoppingcart.CartItemDto;
import mmosii.bookstore.dto.shoppingcart.ShoppingCartDto;
import mmosii.bookstore.model.CartItem;
import mmosii.bookstore.model.ShoppingCart;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toCartItemDto(CartItem cartItem);
}
