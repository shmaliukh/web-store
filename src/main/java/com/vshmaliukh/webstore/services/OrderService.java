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

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.ORDER_STATUS_COMPLETED_STR;


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
            List<OrderItem> orderItemList = orderItemService.readOrderItemListByOrder(order);
            return orderItemList.stream()
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

    public int calcOrderTotalSum(Order order) {
        if (isValidEntity(order)) {
            List<OrderItem> orderItemList = order.getOrderItemList();
            if (orderItemList != null) {
                return orderItemList.stream()
                        .filter(OrderItem::isActive)
                        .mapToInt(o -> o.getOrderItemPrice() * o.getQuantity())
                        .sum();
            } else {
                log.error("problem to calculate order total sum // order item list is NULL");
            }
        } else {
            log.error("problem to calculate order total sum // invalid order: '{}'", order);
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

    public void setUpSoldQuantityIfOrderIsCompleted(Order order) {
        if (isValidEntity(order)) {
            if (order.getStatus().equalsIgnoreCase(ORDER_STATUS_COMPLETED_STR)) {
                List<OrderItem> orderItems = orderItemService.readOrderItemListByOrder(order);
                for (OrderItem orderItem : orderItems) {
                    Item item = orderItem.getItem();
                    item.setSoldOutQuantity(orderItem.getQuantity());
                    itemService.saveItem(item);
                }
                log.info("successfully save order items as completed, order: '{}'", order);
            }
        } else {
            log.error("problem to set up sold out quantity if order is completed // invalid order");
        }
    }

    public void changeOrderStatus(Optional<Order> optionalOrder, String newStatusStr) {
        if (optionalOrder.isPresent() && StringUtils.isNotBlank(newStatusStr)) {
            Order order = optionalOrder.get();
            order.setStatus(newStatusStr);
            orderRepository.save(order);
            log.info("changed order status, new status: '{}'", newStatusStr);
        } else {
            log.warn("problem to change order status // status str: '{}'", newStatusStr);
            log.error("problem to change order status"
                    + (!optionalOrder.isPresent() ? " // not found order" : " // new status is empty"));
        }
    }

    public Optional<Order> createEmptyOrder(Long userId, String status, String comment) {
        Optional<User> optionalUser = userService.readUserById(userId);
        if (optionalUser.isPresent() && StringUtils.isNotBlank(status)) {
            Order order = new Order();
            order.setDateCreated(new Date());
            order.setUser(optionalUser.get());
            order.setStatus(status);
            order.setComment(comment);
            order.setOrderItemList(Collections.emptyList());
            return Optional.of(order);
        }
        log.error("problem to created empty order"
                + (!optionalUser.isPresent() ? " // user not found" : " // status is blank"));
        return Optional.empty();
    }

    public List<Order> findUserOrderList(User user) {
        return Collections.unmodifiableList(orderRepository.findAllByUser(user));
    }

}
