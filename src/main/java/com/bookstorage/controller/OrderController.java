package com.bookstorage.controller;

import com.bookstorage.dto.order.OrderRequestDto;
import com.bookstorage.dto.order.OrderResponseDto;
import com.bookstorage.dto.order.OrderUpdateRequestDto;
import com.bookstorage.dto.orderitem.OrderItemResponseDto;
import com.bookstorage.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API", description = "API for managing orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "create Order", description = "This method creates a new order.")
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public OrderResponseDto createOrder(@RequestBody @Valid OrderRequestDto requestDto) {
        return orderService.createOrder(requestDto);
    }

    @Operation(summary = "Get All Orders", description = "This method provides all orders"
            + " that can be viewed by turning the pages")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Page<OrderResponseDto> getOrder(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @Operation(summary = "Update order", description = "Method for updating the order status")
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public OrderResponseDto updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid OrderUpdateRequestDto requestDto
    ) {
        return orderService.updateStatus(id, requestDto);
    }

    @Operation(summary = "Get order items", description = "The method provides all order items "
            + "within the order that can be viewed by turning the pages.")
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Page<OrderItemResponseDto> getOrderItemsInOrder(
            @PathVariable Long orderId,
            Pageable pageable
    ) {
        return orderService.getOrderItemInOrder(orderId, pageable);
    }

    @Operation(summary = "Get order item", description = "The method allows you to see "
            + "a specific order item in the current order")
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public OrderItemResponseDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderService.getOrderItem(orderId, itemId);
    }
}
