package com.bookstorage.repository;

import com.bookstorage.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);

    Optional<Order> findByIdAndUserId(Long id, Long userid);
}
