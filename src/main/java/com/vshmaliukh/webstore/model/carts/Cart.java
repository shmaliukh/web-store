package com.vshmaliukh.webstore.model.carts;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "cart")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserCart.class, name = "userCart"),
        @JsonSubTypes.Type(value = UnauthorizedUserCart.class, name = "unauthorizedUserCart"),
})
public abstract class Cart {

    @Id
    @Column(name = CART_ID_COLUMN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne // todo check type
    @Column(name = ITEM_ID_COLUMN, nullable = false)
    private Item item;

    @Column(name = QUANTITY_COLUMN, nullable = false)
    private Integer itemQuantity;

    @Column(name = PRICE_COLUMN,nullable = false) // todo check its need
    private int price;

}
