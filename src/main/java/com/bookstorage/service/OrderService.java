package com.bookstorage.service;

import com.bookstorage.dto.order.OrderPatchRequestDto;
import com.bookstorage.dto.order.OrderRequestDto;
import com.bookstorage.dto.order.OrderResponseDto;
import com.bookstorage.dto.orderitem.OrderItemResponseDto;
import java.util.List;

public interface OrderService {

    OrderResponseDto createOrder(OrderRequestDto requestDto);

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto updateStatus(Long orderId, OrderPatchRequestDto requestDto);

    List<OrderItemResponseDto> getOrderItemInOrder(Long orderId);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId);
}
