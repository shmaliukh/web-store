package com.vshmaliukh.webstore.model.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vshmaliukh.webstore.model.AuditModel;
import com.vshmaliukh.webstore.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = ORDER_ITEM_TABLE)
public class OrderItem extends AuditModel {

    @Id
    @Column(name = ORDER_ITEM_ID_COLUMN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    private int quantity;

    private int orderItemPrice;

    private boolean active;

    @ManyToOne
    private Item item;

    @ManyToOne
    @JsonIgnore
    private Order order;

}
