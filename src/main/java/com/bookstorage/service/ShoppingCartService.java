package com.bookstorage.service;

import com.bookstorage.dto.cartitem.CartItemRequestDto;
import com.bookstorage.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstorage.model.User;

public interface ShoppingCartService {

    ShoppingCartResponseDto addBook(CartItemRequestDto requestDto);

    ShoppingCartResponseDto getShoppingCartByUser();

    ShoppingCartResponseDto updateCartItem(Long id, CartItemRequestDto requestDto);

    void deleteCartItem(Long id);

    void addUserToShopCart(User user);
}
