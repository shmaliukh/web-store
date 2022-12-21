package com.vshmaliukh.webstore.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@Entity
@Table(name = CART_TABLE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cartProducts")
public class Cart {

    @Id
    @Column(name = CART_ID_COLUMN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @JoinTable(name = USER_TABLE)
    @Column(name = USER_ID_COLUMN, nullable = false)
    private Long userId;

    @JoinTable // item table? - todo ask Vlad
    @Column(name = CATEGORY_COLUMN, nullable = false)
    private String category;


    @Column(name = ITEM_ID_COLUMN, nullable = false)
    private Integer itemId;

    @Column(name = QUANTITY_COLUMN, nullable = false)
    private Integer itemQuantity;

    // todo implement relationships

}
