package com.vshmaliukh.webstore.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@Entity
@Table(name = CART_TABLE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cartProducts")
public class Cart {

    @Id
    @Column(name = ORDER_ID_COLUMN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column(name = USER_ID_COLUMN, nullable = false)
    private Long userId;

    @Column(name = CATEGORY_ID_COLUMN, nullable = false)
    private Long categoryId;

    @Column(name = ITEM_ID_COLUMN, nullable = false)
    private Long itemId;

    @Column(name = QUANTITY_COLUMN, nullable = false)
    private Integer itemQuantity;

    // todo implement relationships

}
