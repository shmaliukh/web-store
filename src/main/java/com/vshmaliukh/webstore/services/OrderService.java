package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.OrderItem;
import com.vshmaliukh.webstore.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.ORDER_STATUS_COMPLETED;


@Slf4j
@Service
@Getter
@AllArgsConstructor
public class OrderService {

    final ItemService itemService;
    final UserService userService;
    final OrderRepository orderRepository;
    final OrderItemService orderItemService;

    public void setUpItemAvailableQuantity(OrderItem orderItem, int oldOrderItemQuantity) {
        Item item = orderItem.getItem();
        if (item != null) {
            int newQuantity = orderItem.getQuantity();
            int availableToBuyQuantity = item.getAvailableToBuyQuantity() + oldOrderItemQuantity - newQuantity;
            item.setAvailableToBuyQuantity(availableToBuyQuantity);
            if (newQuantity < 1) {
                orderItem.setActive(false);
            }
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
                OrderItem orderItem = orderItemService.formOrderItem(quantity, item, order);

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
        if (item != null && orderItem != null) {
            int itemAvailableToBuyQuantity = item.getAvailableToBuyQuantity();
            int availableToBuyQuantity = itemAvailableToBuyQuantity - quantity;
            item.setAvailableToBuyQuantity(availableToBuyQuantity);
            itemService.saveItem(item);
            log.info("set up item '{}' available quantity to buy: {} ", item.getName(), availableToBuyQuantity);
        } else {
            log.warn("problem to set up item available to buy quantity // item: '{}', orderITem: '{}'", item, orderItem);
        }
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
        setUpSoldQuantityIfOrderIsCompleted(order);
        orderRepository.save(order);
    }

    private void setUpSoldQuantityIfOrderIsCompleted(Order order) {
        if (order.getStatus().equals(ORDER_STATUS_COMPLETED)) {
            List<OrderItem> orderItems = orderItemService.readOrderItemsByOrder(order);
            for (OrderItem orderItem : orderItems) {
                Item item = orderItem.getItem();
                item.setSoldOutQuantity(orderItem.getQuantity());
                itemService.saveItem(item);
            }
        }
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
                    .mapToInt(Item::getSalePrice)
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
            order.setOrderItemList(Collections.emptyList());
            return Optional.of(order);
        }
        return Optional.empty();
    }

    public List<Order> findUserOrderList(User user) {
        return orderRepository.findAllByUser(user);
    }

}
