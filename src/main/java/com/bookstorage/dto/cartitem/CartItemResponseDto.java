package com.bookstorage.dto.cartitem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponseDto {
    private Long id;
    private Long shoppingCartId;
    private Long bookId;
    private int quantity;
}
