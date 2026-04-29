package com.bookstorage.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDto {
    @Positive
    private Long bookId;
    @Positive
    private int quantity;
}
