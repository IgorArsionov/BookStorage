package com.bookstorage.mapper;

import com.bookstorage.config.MapperConfig;
import com.bookstorage.dto.cartitem.CartItemRequestDto;
import com.bookstorage.dto.cartitem.CartItemResponseDto;
import com.bookstorage.model.CartItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {

    @Mapping(target = "shoppingCartId", source = "shoppingCart.id")
    @Mapping(target = "bookId", source = "book.id")
    CartItemResponseDto toDto(CartItem cartItem);

    CartItem toEntity(CartItemRequestDto requestDto);

    @Mapping(target = "book", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCartItem(@MappingTarget CartItem cartItem, CartItemRequestDto requestDto);

}
