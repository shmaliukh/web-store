package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.OrderItem;
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
    final UserService userService;
    final OrderRepository orderRepository;
    final OrderItemService orderItemService;

    public void setUpAvailableItemQuantity(OrderItem orderItem, int oldOrderItemQuantity, Integer newQuantity, boolean orderItemPreviousState) {
        Item item = orderItem.getItem();
        if (item != null) {
            int quantityToSet;
            // FIXME fix wrong adding quantity to item entity when change order item 'quantity' and set up 'active' = false
            if (!orderItemPreviousState) {
                quantityToSet = item.getQuantity() - oldOrderItemQuantity;
            } else if (!orderItem.isActive()) {
                quantityToSet = item.getQuantity() + newQuantity;
            } else {
                quantityToSet = item.getQuantity() - (newQuantity - oldOrderItemQuantity);
            }
            item.setQuantity(quantityToSet);
            itemService.saveItem(item);
        }
    }

    public void insertItemToOrder(Long orderId, Integer itemId, Integer quantity) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Optional<Item> optionalItem = itemService.readItemById(itemId);
            if (optionalItem.isPresent()) {
                Item item = optionalItem.get();
                OrderItem orderItem = OrderItemService.formOrderItem(quantity, item, order);

                orderItemService.save(orderItem);

                setUpItemAvailableToBuyQuantity(quantity, item, orderItem);
            } else {
                log.warn("problem to insert item to order // not found item by '{}' id", itemId);
            }
        } else {
            log.warn("problem to insert item to order // not found order by '{}' id", orderId);
        }
    }

    private void setUpItemAvailableToBuyQuantity(Integer quantity, Item item, OrderItem orderItem) {
        int availableToBuyQuantity = item.getQuantity() - orderItem.getQuantity();
        item.setQuantity(availableToBuyQuantity);
        itemService.saveItem(item);
        log.info("sold '{}' // available to buy '{}' item: '{}'", quantity, item, availableToBuyQuantity);
    }

    public Integer calcTotalOrderItems(Order order) {
        List<OrderItem> orderProducts = orderItemService.readOrderItemsByOrder(order);
        if (orderProducts != null) {
            return orderProducts.stream()
                    .mapToInt(OrderItem::getQuantity)
                    .sum();
        }
        return 0;
    }

    public OrderItem readOrderItemById(Long id) {
        Optional<OrderItem> optionalOrderItem = orderItemService.readOrderItemByOrderItemId(id);
        if (optionalOrderItem.isPresent()) {
            return optionalOrderItem.get();
        } else {
            log.warn("problem to find order item by '{}' id", id);
            return null;
        }
    }

    public Integer calcTotalOrderPrice(Order order) {
        List<OrderItem> orderProducts = orderItemService.readOrderItemsByOrder(order);
        if (orderProducts != null) {
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
        return orderItemService.readOrderItemsByOrder(order);
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

    public void saveOrderItem(OrderItem orderItem) {
        orderItemService.save(orderItem);
    }

    public Optional<Order> createEmptyOrder(Long userId, String status, String comment) {
        Optional<User> optionalUser = userService.readUserById(userId);
        if (optionalUser.isPresent()) {
            Order order = new Order();
            order.setDateCreated(new Date());
            order.setUser(optionalUser.get());
            order.setStatus(status);
            order.setComment(comment);
            order.setItemList(Collections.emptyList());
            return Optional.of(order);
        }
        return Optional.empty();
    }

    public List<Order> findUserOrderList(User user) {
        return orderRepository.findAllByUser(user);
    }

}
