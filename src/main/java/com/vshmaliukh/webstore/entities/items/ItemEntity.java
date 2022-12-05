package com.vshmaliukh.webstore.entities.items;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@MappedSuperclass
public abstract class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_COLUMN, nullable = false)
    private Integer id;

    @Column(name = CATEGORY_COLUMN, nullable = false)
    String category;

    @Column(name = NAME_COLUMN, nullable = false)
    String name;

    @Column(name = PRICE_COLUMN, nullable = false)
    int price;

    @Column(name = QUANTITY_COLUMN, nullable = false)
    int quantity;

    @Column(name = IS_AVAILABLE_IN_STORE_COLUMN, nullable = false)
    boolean isAvailableInStore;

}
