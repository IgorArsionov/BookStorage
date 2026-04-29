package com.bookstorage.service.impl;

import com.bookstorage.dto.cartitem.CartItemRequestDto;
import com.bookstorage.dto.cartitem.CartItemResponseDto;
import com.bookstorage.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.mapper.CartItemMapper;
import com.bookstorage.mapper.ShoppingCartMapper;
import com.bookstorage.model.CartItem;
import com.bookstorage.model.ShoppingCart;
import com.bookstorage.model.User;
import com.bookstorage.repository.BookRepository;
import com.bookstorage.repository.CartItemRepository;
import com.bookstorage.repository.ShoppingCartRepository;
import com.bookstorage.service.ShoppingCartService;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public CartItemResponseDto addBook(CartItemRequestDto requestDto) {
        if (!bookRepository.existsById(requestDto.getBookId())) {
            throw new EntityNotFoundException("Can't find book by id: " + requestDto.getBookId());
        }

        String authenticationEmail = getAuthenticationEmail();

        ShoppingCart shoppingCartByUserEmail =
                shoppingCartRepository.findByUserEmail(authenticationEmail)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Can't find ShoppingCart by user email: " + authenticationEmail
                        ));
        Optional<CartItem> first = shoppingCartByUserEmail.getCartItems()
                .stream()
                .filter(c -> Objects.equals(c.getBook().getId(), requestDto.getBookId()))
                .findFirst();

        CartItem cartItem;
        if (first.isPresent()) {
            cartItem = first.get();
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        } else {
            cartItem = cartItemMapper.toEntity(requestDto);
            cartItem.setShoppingCart(shoppingCartByUserEmail);
            shoppingCartByUserEmail.getCartItems().add(cartItem);
        }

        CartItem cartItemFromDb = cartItemRepository.save(cartItem);

        shoppingCartRepository.save(shoppingCartByUserEmail);

        return cartItemMapper.toDto(cartItemFromDb);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartByUser() {
        String authenticationEmail = getAuthenticationEmail();
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserEmail(authenticationEmail)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find ShoppingCart by user email: " + authenticationEmail
                )));
    }

    @Override
    public CartItemResponseDto updateCartItem(Long id, CartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find cartItem by id: " + id));
        cartItemMapper.updateCartItem(cartItem, requestDto);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    private String getAuthenticationEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getEmail();
    }
}
