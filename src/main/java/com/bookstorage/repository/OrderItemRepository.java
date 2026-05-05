package com.bookstorage.repository;

import com.bookstorage.model.OrderItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> getAllByOrderId(Long orderId, Pageable pageable);

    Optional<OrderItem> getByIdAndOrderId(Long id, Long orderId);
}
