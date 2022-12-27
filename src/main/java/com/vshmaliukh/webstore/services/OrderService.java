package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.OrderItem;
import com.vshmaliukh.webstore.repositories.OrderItemRepository;
import com.vshmaliukh.webstore.repositories.OrderRepository;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Getter
@AllArgsConstructor
public class OrderService {

    final OrderRepository orderRepository;
    final OrderItemRepository orderItemRepository;

    public void saveOrderItem(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }

    public OrderItem readOrderItemById(Long id){
        Optional<OrderItem> optionalOrderItem = orderItemRepository.readOrderItemByOrderItemId(id);
        if(optionalOrderItem.isPresent()){
            return optionalOrderItem.get();
        } else {
            log.warn("problem to find order item by '{}' id", id);
            return null;
        }
    }

    public Integer calcTotalOrderPrice(Order order) {
        List<OrderItem> orderProducts = orderItemRepository.readOrderItemsByOrder(order);
        if(orderProducts != null){
            return orderProducts.stream()
                    .filter(OrderItem::isActive)
                    .map(orderItem -> orderItem.getOrderItemPrice() * orderItem.getQuantity())
                    .mapToInt(Integer::intValue).sum();
        }
        return 0;
    }

    public Order readOrderById(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.orElse(null);
    }

    public List<OrderItem> readOrderItemListByOrderId(Long id) {
        Order order = readOrderById(id);
        return orderItemRepository.readOrderItemsByOrder(order);
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public void changeOrderStatus(long userId, String newStatusStr) {
        Order orderByUserId = readOrderByUserId(userId);
        if (orderByUserId != null && StringUtils.isNotBlank(newStatusStr)) {
            orderByUserId.setStatus(newStatusStr);
            orderRepository.save(orderByUserId);
            log.info("userId: '{}' // changed order status, new status: '{}'", userId, newStatusStr);
        } else {
            log.warn("userId: '{}' // problem to change order status // order: '{} // status str: '{}'", userId, orderByUserId, newStatusStr);
        }
    }

    public int calcOrderTotalSumByUserId(long orderId) {
        List<Item> itemListByUserId = readOrderItemListByUserId(orderId);
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
        if (readOrderByUserId != null) {
            // FIXME
//            return readOrderByUserId.getItemList();
        }
        log.warn("userId: '{}' // order item list is empty", userId);
        return Collections.emptyList();
    }

    public Order readOrderByUserId(long userId) {
        return orderRepository.findByUserId(userId);
    }

}
