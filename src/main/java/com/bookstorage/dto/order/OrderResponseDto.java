package com.bookstorage.dto.order;

import com.bookstorage.dto.orderitem.OrderItemResponseDto;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private String status;
    private BigDecimal total;
    private String orderDate;
    private Set<OrderItemResponseDto> orderItems;
}
