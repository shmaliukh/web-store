package com.vshmaliukh.webstore.model.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vshmaliukh.webstore.model.AuditModel;
import com.vshmaliukh.webstore.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Order_items")
public class OrderItem extends AuditModel {

    public static final int DEFAULT_PRICE = 0;
    public static final int DEFAULT_QUANTITY = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    private int quantity;

    private int orderItemPrice;

    private boolean active;

    @ManyToOne
    private Item item;

    @ManyToOne
    @JsonIgnore
    private Order order;

    public void setPrice(int price) {
        if (price >= 0) {
            this.orderItemPrice = price;
        } else {
            this.orderItemPrice = DEFAULT_PRICE;
            log.warn("order item id: '{}' // invalid price value to set: '{}' // set up default orderItemPrice value: '{}'",
                    getOrderItemId(), price, DEFAULT_PRICE);
        }
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            this.quantity = DEFAULT_QUANTITY;
            log.warn("order item id: '{}' // invalid quantity value to set: '{}' // set up default quantity value: '{}'",
                    getOrderItemId(), quantity, DEFAULT_QUANTITY);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity
                && orderItemPrice == orderItem.orderItemPrice
                && active == orderItem.active
                && Objects.equals(orderItemId, orderItem.orderItemId)
                && Objects.equals(item, orderItem.item)
                && Objects.equals(order, orderItem.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemId, quantity, orderItemPrice, active, item, order);
    }

}
