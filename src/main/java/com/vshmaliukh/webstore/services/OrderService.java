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
public class OrderService implements EntityValidator<Order> {

    final ItemService itemService;
    final UserService userService;
    final OrderRepository orderRepository;
    final OrderItemService orderItemService;

    public void setUpItemAvailableQuantity(OrderItem orderItem, int oldOrderItemQuantity) {
        // TODO refactor
        Item item = orderItem.getItem();
        if (orderItemService.isValidEntity(orderItem) && itemService.isValidEntity(item)) {
            int newQuantity = orderItem.getQuantity();
            int availableToBuyQuantity = item.getAvailableToBuyQuantity() + oldOrderItemQuantity - newQuantity;
            item.setAvailableToBuyQuantity(availableToBuyQuantity);
            if (newQuantity < 1) {
                orderItem.setActive(false);
            }
            itemService.saveItem(item);
        } else {
            log.error("problem to set up item available quantity"
                    + (!orderItemService.isValidEntity(orderItem) ? " // invalid order item" : " // invalid item"));
        }
    }

    public void insertItemToOrder(Order order, Item item, int quantity) {
        if (isValidEntity(order) && itemService.isValidEntity(item)) {
            OrderItem orderItem = orderItemService.formOrderItem(quantity, item, order);
            orderItemService.save(orderItem);
            setUpItemAvailableToBuyQuantity(quantity, item, orderItem);
        } else {
            log.warn("problem to insert item to order"
                    + (!isValidEntity(order) ? (" // invalid order") : " // invalid item"));
            log.trace("order: '{}', item: '{}'", order, item);
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

    public int calcTotalOrderItemQuantity(Order order) {
        if (isValidEntity(order)) {
            return orderItemService.readOrderItemListByOrder(order).stream()
                    .mapToInt(OrderItem::getQuantity)
                    .sum();
        }
        log.error("problem to calc order items quantity // invalid order");
        return 0;
    }

    public Optional<OrderItem> readOrderItemById(Long id) {
        if (id != null && id > 0) {
            return orderItemService.readOrderItemByOrderItemId(id);
        }
        log.error("problem to read order item"
                + (id == null ? " // id is NULL" : " // id < 1"));
        return Optional.empty();
    }

    public Integer calcTotalOrderPrice(Order order) {
        List<OrderItem> orderProducts = orderItemService.readOrderItemListByOrder(order);
        if (orderProducts != null) {
            return orderProducts.stream()
                    .filter(OrderItem::isActive)
                    .map(orderItem -> orderItem.getOrderItemPrice() * orderItem.getQuantity())
                    .mapToInt(Integer::intValue).sum();
        }
        return 0;
    }

    public Optional<Order> readOrderById(Long id) {
        if (id != null && id > 0) {
            return orderRepository.findById(id);
        } else {
            log.error("problem to read order by id"
                    + (id == null ? " // id is NULL" : " // id < 1"));
            return Optional.empty();
        }
    }

    public List<OrderItem> readOrderItemListByOrderId(Long id) {
        if (id != null && id > 0) {
            Optional<Order> optionalOrder = readOrderById(id);
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                return Collections.unmodifiableList(orderItemService.readOrderItemListByOrder(order));
            }
        }
        log.error("problem to read order item list by order id"
                + (id == null ? " // id is NULL" : " // id < 1"));
        return Collections.emptyList();

    }

    public void save(Order order) {
        if (isValidEntity(order)) {
            setUpSoldQuantityIfOrderIsCompleted(order);
            orderRepository.save(order);
            log.info("saved order: {}", order);
        } else {
            log.error("problem to save order");
        }
    }

    private void setUpSoldQuantityIfOrderIsCompleted(Order order) {
        if (isValidEntity(order) && order.getStatus().equalsIgnoreCase(ORDER_STATUS_COMPLETED)) {
            List<OrderItem> orderItems = orderItemService.readOrderItemListByOrder(order);
            for (OrderItem orderItem : orderItems) {
                Item item = orderItem.getItem();
                item.setSoldOutQuantity(orderItem.getQuantity());
                itemService.saveItem(item);
            }
            log.info("successfully save order items as completed, order: '{}'", order);
        }
    }

    public void changeOrderStatus(long orderId, String newStatusStr) {
        Optional<Order> optionalOrder = readOrderByUserId(orderId);
        if (optionalOrder.isPresent() && StringUtils.isNotBlank(newStatusStr)) {
            Order orderByUserId = optionalOrder.get();
            orderByUserId.setStatus(newStatusStr);
            orderRepository.save(orderByUserId);
            log.info("orderId: '{}' // changed order status, new status: '{}'", orderId, newStatusStr);
        } else {
            log.warn("orderId: '{}' // problem to change order status // status str: '{}'", orderId, newStatusStr);
            log.error("problem to change order status"
                    + (!optionalOrder.isPresent() ? " // not found order by id: '" + orderId + "'" : " // new status is empty"));
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
        Optional<Order> optionalOrder = readOrderByUserId(userId);
        if (optionalOrder.isPresent()) {
            // FIXME
//            return readOrderByUserId.getItemList();
        }
        log.warn("userId: '{}' // order item list is empty", userId);
        return Collections.emptyList();
    }

    public Optional<Order> readOrderByUserId(long userId) {
        return orderRepository.findByUserId(userId);
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
        return Collections.unmodifiableList(orderRepository.findAllByUser(user));
    }

}
