package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.OrderRepository;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderService {

    final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void changeOrderStatus(long userId, String newStatusStr) {
        Order orderByUserId = readOrderByUserId(userId);
        orderByUserId.setStatus(newStatusStr);
        orderRepository.save(orderByUserId);
        log.info("userId: '{}' // changed order status, new status: '{}'", userId, newStatusStr);
    }

    public void addItemToOrder(long userId, Item item) {
        Order orderByUserId = readOrderByUserId(userId);
        orderByUserId.getItemList().add(item);
        orderRepository.save(orderByUserId);
        log.info("userId: '{}' // added new '{}' item to order", userId, item);
    }

    public int calcOrderTotalSumByUserId(long userId) {
        List<Item> itemListByUserId = readOrderItemListByUserId(userId);
        if (itemListByUserId != null) {
            return itemListByUserId.stream()
                    .filter(Item::isAvailableInStore)
                    .mapToInt(Item::getPrice)
                    .sum();
        }
        return 0;
    }

    public List<Item> readOrderItemListByUserId(long userId) {
        Order readOrderByUserId = readOrderByUserId(userId);
        if(readOrderByUserId != null){
            return readOrderByUserId.getItemList();
        }
        log.warn("userId: '{}' // order item list is empty", userId);
        return Collections.emptyList();
    }

    public Order readOrderByUserId(long userId) {
        return orderRepository.findByUserId(userId);
    }

    public void deleteOrderByUserId(long userId) {
        orderRepository.deleteOrderByUserId(userId);
    }

}
