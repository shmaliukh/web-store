package com.vshmaliukh.webstore.model.items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

import static com.vshmaliukh.webstore.ConstantsForEntities.ORDER_ITEM_TABLE;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id", nullable = false)
    private Integer id;

    @OneToOne
    private Item item;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private int price; // todo change type

    public void setQuantity(int quantity) {

        // todo mb implement quantity check

        if(quantity>0){
            this.quantity = quantity;
        } else{
            this.quantity = 1;
        }
    }
}
