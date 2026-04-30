package com.bookstorage.service.impl;

import com.bookstorage.dto.cartitem.CartItemRequestDto;
import com.bookstorage.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.mapper.CartItemMapper;
import com.bookstorage.mapper.ShoppingCartMapper;
import com.bookstorage.model.Book;
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
    public ShoppingCartResponseDto addBook(CartItemRequestDto requestDto) {

        ShoppingCart shoppingCartByUserId = getShoppingCartByUserId();

        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id: " + requestDto.getBookId()));

        Optional<CartItem> first = shoppingCartByUserId.getCartItems()
                .stream()
                .filter(c -> Objects.equals(c.getBook().getId(), book.getId()))
                .findFirst();

        if (first.isPresent()) {
            CartItem cartItem = first.get();
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        } else {
            CartItem cartItem = cartItemMapper.toEntity(requestDto);
            cartItem.setBook(book);
            cartItem.setShoppingCart(shoppingCartByUserId);
            shoppingCartByUserId.getCartItems().add(cartItem);
        }
        ShoppingCart save = shoppingCartRepository.save(shoppingCartByUserId);
        return shoppingCartMapper.toDto(save);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartByUser() {
        return shoppingCartMapper.toDto(getShoppingCartByUserId());
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(Long id, CartItemRequestDto requestDto) {

        ShoppingCart shoppingCartByUserId = getShoppingCartByUserId();

        CartItem cartItem = getCartByUdAndByShoppingCartId(id, shoppingCartByUserId.getId());

        cartItemMapper.updateCartItem(cartItem, requestDto);

        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCartByUserId));
    }

    @Override
    public void deleteCartItem(Long id) {

        ShoppingCart shoppingCartByUserId = getShoppingCartByUserId();

        CartItem cartItem = getCartByUdAndByShoppingCartId(id, shoppingCartByUserId.getId());

        cartItemRepository.deleteById(cartItem.getId());
    }

    @Override
    public void addUserToShopCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart getShoppingCartByUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long authenticationUserId = user.getId();

        return shoppingCartRepository.findByUserId(authenticationUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find ShoppingCart by user id: " + authenticationUserId
                ));
    }

    private CartItem getCartByUdAndByShoppingCartId(Long id, Long shopId) {
        return cartItemRepository
                .findByIdAndShoppingCartId(id, shopId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cartItem by id: " + id
                                + " in ShoppingCart by id: " + shopId
                ));
    }
}
