package com.bookstorage.repository;

import com.bookstorage.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getAllByOrderId(Long orderId);

    Optional<OrderItem> getByIdAndOrderId(Long id, Long orderId);
}
