package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.OrderItem;
import com.vshmaliukh.webstore.repositories.OrderItemRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Service
@AllArgsConstructor
public class OrderItemService implements EntityValidator<OrderItem> {

    @Getter
    final OrderItemRepository orderItemRepository;

    final ItemService itemService;

    public OrderItem formOrderItem(Integer quantity, Item item, Order order) {
        Optional<OrderItem> orderItemByItem = orderItemRepository.findOrderItemByItem(item);
        return orderItemByItem
                .map(orderItem -> generateOrderItemIfExist(quantity, orderItem))
                .orElseGet(() -> generateOrderItemIfNotExist(quantity, item, order));
    }

    public OrderItem generateOrderItemIfNotExist(Integer quantity, Item item, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemPrice(Math.max(item.getSalePrice(), 0));
        orderItem.setQuantity(quantity != null && quantity > 0 ? quantity : 0);
        orderItem.setActive(true);
        orderItem.setOrder(order);
        orderItem.setItem(item);
        return orderItem;
    }

    public OrderItem generateOrderItemIfExist(Integer quantity, OrderItem orderItem) {
        int orderItemQuantity = orderItem.getQuantity();
        orderItem.setQuantity(orderItemQuantity + quantity);
        if (orderItem.getQuantity() >= 1) {
            orderItem.setActive(false);
        }
        return orderItem;
    }

    public void save(OrderItem orderItem) {
        if (isValidEntity(orderItem)) {
            if (orderItem.getQuantity() < 1) {
                log.warn("order item quantity < 1, set active state as 'false' // orderItem: '{}'", orderItem);
                orderItem.setActive(false);
            }
            orderItemRepository.save(orderItem);
            log.info("saved orderItem: {}", orderItem);
        } else {
            log.error("problem to save order item // invalid orderItem");
        }
    }

    public List<OrderItem> readOrderItemListByOrder(Order order) {
        return Collections.unmodifiableList(orderItemRepository.readOrderItemsByOrder(order));
    }

    public Optional<OrderItem> readOrderItemByOrderItemId(Long id) {
        return orderItemRepository.readOrderItemByOrderItemId(id);
    }

}
