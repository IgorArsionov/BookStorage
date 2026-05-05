package com.bookstorage.controller;

import com.bookstorage.dto.order.OrderRequestDto;
import com.bookstorage.dto.order.OrderResponseDto;
import com.bookstorage.dto.order.OrderUpdateRequestDto;
import com.bookstorage.dto.orderitem.OrderItemResponseDto;
import com.bookstorage.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto requestDto) {
        System.out.println(requestDto.getShippingAddress());
        return orderService.createOrder(requestDto);
    }

    @GetMapping
    public Page<OrderResponseDto> getOrder(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @PatchMapping("/{id}")
    public OrderResponseDto updateStatus(
            @PathVariable Long id,
            @RequestBody OrderUpdateRequestDto requestDto
    ) {
        return orderService.updateStatus(id, requestDto);
    }

    @GetMapping("/{orderId}/items")
    public Page<OrderItemResponseDto> getOrderItemsInOrder(
            @PathVariable Long orderId,
            Pageable pageable
    ) {
        return orderService.getOrderItemInOrder(orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderService.getOrderItem(orderId, itemId);
    }
}
