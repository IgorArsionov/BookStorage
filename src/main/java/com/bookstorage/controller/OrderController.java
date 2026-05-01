package com.bookstorage.controller;

import com.bookstorage.dto.order.OrderPatchRequestDto;
import com.bookstorage.dto.order.OrderRequestDto;
import com.bookstorage.dto.order.OrderResponseDto;
import com.bookstorage.dto.orderitem.OrderItemResponseDto;
import com.bookstorage.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    public List<OrderResponseDto> getOrder() {
        return orderService.getAllOrders();
    }

    @PatchMapping("/{id}")
    public OrderResponseDto updateStatus(
            @PathVariable Long id,
            @RequestBody OrderPatchRequestDto requestDto
    ) {
        return orderService.updateStatus(id, requestDto);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDto> getOrderItemsInOrder(@PathVariable Long orderId) {
        return orderService.getOrderItemInOrder(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderService.getOrderItem(orderId, itemId);
    }
}
