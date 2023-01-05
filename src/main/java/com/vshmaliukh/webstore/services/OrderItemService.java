package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.OrderItem;
import com.vshmaliukh.webstore.repositories.OrderItemRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Service
@AllArgsConstructor
public class OrderItemService {

    final ItemService itemService;
    final OrderItemRepository orderItemRepository;

    static OrderItem formOrderItem(Integer quantity, Item item, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemPrice(item.getSalePrice());
        orderItem.setQuantity(quantity);

        orderItem.setActive(orderItem.getQuantity() >= 1);

        orderItem.setOrder(order);
        orderItem.setItem(item);

        return orderItem;
    }


    public void save(OrderItem orderItem) {
        if (orderItem != null) {
            if (orderItem.getQuantity() < 1) {
                orderItem.setActive(false);
            }
            orderItemRepository.save(orderItem);
        } else {
            log.warn("problem to save order item // orderItem == NULL");
        }
    }

    public List<OrderItem> readOrderItemsByOrder(Order order) {
        return orderItemRepository.readOrderItemsByOrder(order);
    }

    public Optional<OrderItem> readOrderItemByOrderItemId(Long id) {
        return orderItemRepository.readOrderItemByOrderItemId(id);
    }
}
