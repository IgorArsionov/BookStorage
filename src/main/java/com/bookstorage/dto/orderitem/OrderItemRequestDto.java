package com.bookstorage.dto.orderitem;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequestDto {
    private Long orderId;
    private String bookId;
    private int quantity;
    private BigDecimal prise;
}
