package com.bookstorage.mapper;

import com.bookstorage.config.MapperConfig;
import com.bookstorage.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstorage.model.CartItem;
import com.bookstorage.model.ShoppingCart;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItemIds", source = "cartItems", qualifiedByName = "cartItemIds")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    @Named("cartItemIds")
    default Set<Long> cartItemIds(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toSet());
    }

}
