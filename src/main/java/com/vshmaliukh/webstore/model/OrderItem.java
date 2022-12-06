package com.vshmaliukh.webstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vshmaliukh.webstore.model.items.Item;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static com.vshmaliukh.webstore.ConstantsForEntities.ORDER_ID_COLUMN;

@Getter
@Setter
@Entity
public class OrderItem {

    @EmbeddedId
    @JsonIgnore
    private OrderItemPK orderProductPK;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = ORDER_ID_COLUMN)
    private Order order;

    @Transient
    public Item getItem() {
        return this.orderProductPK.getItem();
    }

    @Transient
    public int getTotalPrice() {
        return getItem().getPrice() * getQuantity();
    }

}
