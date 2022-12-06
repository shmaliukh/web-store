package com.vshmaliukh.webstore.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vshmaliukh.webstore.model.items.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import static com.vshmaliukh.webstore.ConstantsForEntities.ITEM_ID_COLUMN;
import static com.vshmaliukh.webstore.ConstantsForEntities.ORDER_ID_COLUMN;

@Getter
@Setter
@Embeddable
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "order")
public class OrderItemPK implements Serializable {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = ORDER_ID_COLUMN)
    private Order order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = ITEM_ID_COLUMN)
    private Item item;

}
