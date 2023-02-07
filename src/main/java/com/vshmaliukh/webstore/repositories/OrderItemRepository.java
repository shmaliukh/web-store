package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> readOrderItemByItemAndOrder(Item item, Order order);

    Optional<OrderItem> readOrderItemByOrderItemId(Long id);

    List<OrderItem> readOrderItemsByOrder(Order order);

}
