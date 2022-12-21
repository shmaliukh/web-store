package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.items.Item;
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

    final ItemService itemService;
    final OrderRepository orderRepository;

    public Map<String, Item> readOrderTypeItemMap(Long userId, Date date) {
        Map<String, Item> typeItemMap = new HashMap<>();
        List<Order> orderList = orderRepository.findAllByUserIdAndDateCreated(userId, date);
        if (orderList != null) {
            findItemsByOrders(typeItemMap, orderList);
        } else {
            log.warn("problem to find order by user id '{}' and '{}' date", userId, date);
        }
        return typeItemMap;
    }

    private void findItemsByOrders(Map<String, Item> typeItemMap, List<Order> orderList) {
        for (Order order : orderList) {
            Integer itemId = order.getItemId();
            String itemClassType = order.getItemClassType();
            Item item = itemService.readItemByIdAndType(itemId, itemClassType);
            if (item != null) {
                typeItemMap.put(itemClassType, item);
            }
        }
    }

    public Order readOrderById(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.orElse(null);
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

    public void addItemToOrder(long userId, Item item) {
        Order orderByUserId = readOrderByUserId(userId);
        // FIXME
//        orderByUserId.getItemList().add(item);
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

    public void deleteOrderByUserId(long userId) {
        orderRepository.deleteOrderByUserId(userId);
    }

}
