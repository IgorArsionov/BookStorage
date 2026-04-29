package com.bookstorage.service;

import com.bookstorage.dto.cartitem.CartItemRequestDto;
import com.bookstorage.dto.cartitem.CartItemResponseDto;
import com.bookstorage.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {

    CartItemResponseDto addBook(CartItemRequestDto requestDto);

    ShoppingCartResponseDto getShoppingCartByUser();

    CartItemResponseDto updateCartItem(Long id, CartItemRequestDto requestDto);

    void deleteCartItem(Long id);
}
