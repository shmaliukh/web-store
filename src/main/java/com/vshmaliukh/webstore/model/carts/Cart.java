package com.vshmaliukh.webstore.model.carts;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vshmaliukh.webstore.model.items.CartItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

import java.util.List;

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

    @OneToMany
    private List<CartItem> items;

}
