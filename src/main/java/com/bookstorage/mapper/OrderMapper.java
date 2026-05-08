package com.bookstorage.mapper;

import com.bookstorage.config.MapperConfig;
import com.bookstorage.dto.order.OrderRequestDto;
import com.bookstorage.dto.order.OrderResponseDto;
import com.bookstorage.dto.order.OrderUpdateRequestDto;
import com.bookstorage.model.Order;
import com.bookstorage.model.Status;
import com.bookstorage.model.User;
import java.time.LocalDateTime;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toDto(Order order);

    Order toEntity(OrderRequestDto requestDto, @Context User user);

    @AfterMapping
    default void setDefaults(@MappingTarget Order order, @Context User user) {
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Order updateStatus(@MappingTarget Order order, OrderUpdateRequestDto requestDto);

}
