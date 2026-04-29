package com.bookstorage.repository;

import com.bookstorage.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("""
        SELECT c FROM ShoppingCart c
        LEFT JOIN FETCH c.cartItems
        WHERE c.user.email = :email
            """)
    Optional<ShoppingCart> findByUserEmail(String email);
}
