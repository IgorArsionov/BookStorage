package com.bookstorage.mapper;

import com.bookstorage.config.MapperConfig;
import com.bookstorage.dto.orderitem.OrderItemResponseDto;
import com.bookstorage.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
