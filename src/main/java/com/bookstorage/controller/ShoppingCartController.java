package com.bookstorage.controller;

import com.bookstorage.dto.cartitem.CartItemRequestDto;
import com.bookstorage.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstorage.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart API", description = "API for managing shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(
            summary = "Add book to shopping cart",
            description = "Adds a book to the current user's shopping cart. "
                    + "If the book already exists in the cart, its quantity is increased."
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ShoppingCartResponseDto addBook(@RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addBook(requestDto);
    }

    @Operation(
            summary = "Get current user's shopping cart",
            description = "Returns the shopping cart of the authenticated user with all cart items."
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartService.getShoppingCartByUser();
    }

    @Operation(
            summary = "Update cart item quantity",
            description = "Updates the quantity of a specific cart item in the shopping cart."
    )
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ShoppingCartResponseDto updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemRequestDto requestDto
    ) {
        return shoppingCartService.updateCartItem(cartItemId, requestDto);
    }

    @Operation(
            summary = "Remove item from shopping cart",
            description = "Deletes a cart item from the shopping cart by its ID."
    )
    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }

}
