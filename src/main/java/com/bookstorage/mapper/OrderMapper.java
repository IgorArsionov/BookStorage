package com.bookstorage.mapper;

import com.bookstorage.config.MapperConfig;
import com.bookstorage.dto.order.OrderPatchRequestDto;
import com.bookstorage.dto.order.OrderRequestDto;
import com.bookstorage.dto.order.OrderResponseDto;
import com.bookstorage.model.Order;
import com.bookstorage.model.OrderItem;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItemsIds", source = "orderItems", qualifiedByName = "setOrderItemIds")
    OrderResponseDto toDto(Order order);

    Order toEntity(OrderRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order updateStatus(@MappingTarget Order order, OrderPatchRequestDto requestDto);

    @Named("setOrderItemIds")
    default Set<Long> setOrderItemIds(Set<OrderItem> orderItems) {
        return orderItems.stream().map(OrderItem::getId).collect(Collectors.toSet());
    }
}
