package com.bookstorage.dto.orderitem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDto {
    private Long id;
    private String bookId;
    private int quantity;
}
