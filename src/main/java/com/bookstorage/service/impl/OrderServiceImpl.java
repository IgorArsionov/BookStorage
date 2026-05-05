package com.bookstorage.service.impl;

import com.bookstorage.dto.order.OrderRequestDto;
import com.bookstorage.dto.order.OrderResponseDto;
import com.bookstorage.dto.order.OrderUpdateRequestDto;
import com.bookstorage.dto.orderitem.OrderItemResponseDto;
import com.bookstorage.exception.EmptyShoppingCartException;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.mapper.OrderItemMapper;
import com.bookstorage.mapper.OrderMapper;
import com.bookstorage.model.Order;
import com.bookstorage.model.ShoppingCart;
import com.bookstorage.model.User;
import com.bookstorage.repository.OrderItemRepository;
import com.bookstorage.repository.OrderRepository;
import com.bookstorage.repository.ShoppingCartRepository;
import com.bookstorage.service.OrderService;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        User currentUser = getCurrentUser();
        ShoppingCart currentUserCart = getCurrentUserCart(currentUser.getId());
        if (currentUserCart.getCartItems().isEmpty()) {
            throw new EmptyShoppingCartException("Cannot create order from empty "
                    + "shopping cart with id: " + currentUserCart.getId());
        }
        Order order = orderMapper.toEntity(requestDto, currentUser);
        order.setOrderItems(
                currentUserCart.getCartItems()
                        .stream()
                        .map(cartItem -> orderItemMapper.toOrderItem(cartItem, order))
                        .collect(Collectors.toSet())
        );
        order.setTotal(
                order.getOrderItems().stream()
                        .map(item -> item.getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        Order savedOrder = orderRepository.save(order);
        currentUserCart.getCartItems().clear();
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        User currentUser = getCurrentUser();
        return orderRepository.findAllByUserId(currentUser.getId(), pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderResponseDto updateStatus(Long orderId, OrderUpdateRequestDto requestDto) {
        User currentUser = getCurrentUser();
        Order order = getOrderByIdAndUserId(orderId, currentUser.getId());
        return orderMapper.toDto(orderRepository.save(orderMapper.updateStatus(order, requestDto)));
    }

    @Override
    public Page<OrderItemResponseDto> getOrderItemInOrder(Long orderId, Pageable pageable) {
        User currentUser = getCurrentUser();
        Order order = getOrderByIdAndUserId(orderId, currentUser.getId());
        return orderItemRepository.getAllByOrderId(order.getId(), pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long itemId) {
        User currentUser = getCurrentUser();
        Order order = getOrderByIdAndUserId(orderId, currentUser.getId());
        return orderItemMapper.toDto(
                orderItemRepository.getByIdAndOrderId(itemId, order.getId()).orElseThrow(
                    () -> new EntityNotFoundException(
                        "Item with id " + itemId + " not found for order id: " + orderId
                    )
        ));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    private ShoppingCart getCurrentUserCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find ShoppingCart by user id: " + userId
                ));
    }

    private Order getOrderByIdAndUserId(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order with id " + orderId + " not found for user id: " + userId
                ));
    }
}
