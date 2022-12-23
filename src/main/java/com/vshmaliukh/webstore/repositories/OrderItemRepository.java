package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.items.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
