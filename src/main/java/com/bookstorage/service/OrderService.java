package com.bookstorage.service;

import com.bookstorage.dto.order.OrderRequestDto;
import com.bookstorage.dto.order.OrderResponseDto;
import com.bookstorage.dto.order.OrderUpdateRequestDto;
import com.bookstorage.dto.orderitem.OrderItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto createOrder(OrderRequestDto requestDto);

    Page<OrderResponseDto> getAllOrders(Pageable pageable);

    OrderResponseDto updateStatus(Long orderId, OrderUpdateRequestDto requestDto);

    Page<OrderItemResponseDto> getOrderItemInOrder(Long orderId, Pageable pageable);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId);
}
