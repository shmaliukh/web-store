package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.OrderRepository;
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
        Order orderByUserId = getOrderByUserId(userId);
        orderByUserId.setStatus(newStatusStr);
        saveOrder(orderByUserId);
       
    }

    public void addItemToOrder(Item item) {
        // TODO implement method
    }

    public void saveOrder(Order order) {
        // TODO
        orderRepository.save(order);
    }

    public int calcOrderTotalSumByUserId(long userId) {
        List<Item> itemListByUserId = readOrderItemListByUserId(userId);
        return itemListByUserId.stream()
                .filter(Item::isAvailableInStore)
                .mapToInt(Item::getPrice)
                .sum();
    }

    public List<Item> readOrderItemListByUserId(long userId) {
        return getOrderByUserId(userId).getItemList();
    }

    public Order getOrderByUserId(long userId) {
        return orderRepository.findByUserId(userId);
    }

    public void deleteOrderByUserId(long userId) {
        orderRepository.deleteOrderByUserId(userId);
    }

}
