package com.bookstorage.mapper;

import com.bookstorage.config.MapperConfig;
import com.bookstorage.dto.orderitem.OrderItemResponseDto;
import com.bookstorage.model.CartItem;
import com.bookstorage.model.Order;
import com.bookstorage.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", source = "book")
    @Mapping(target = "price", source = "book.price")
    @Mapping(target = "quantity", source = "quantity")
    OrderItem toOrderItem(CartItem cartItem, @Context Order order);

    @AfterMapping
    default void setOrder(
            @MappingTarget OrderItem orderItem,
            @Context Order order
    ) {
        orderItem.setOrder(order);
    }
}
