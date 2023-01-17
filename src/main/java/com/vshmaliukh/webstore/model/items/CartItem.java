package com.vshmaliukh.webstore.model.items;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@NoArgsConstructor
@Entity
@Table(name = "Cart_item")
public class CartItem {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id", nullable = false)
    private Integer id;

    @OneToOne
    private Item item;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public void setItem(Item item){
        this.item = item;
        this.id = item.getId();
    }

    public void setQuantity(int quantity) {

        // todo mb implement quantity check

        if(quantity>0){
            this.quantity = quantity;
        } else{
            this.quantity = 1;
        }
    }
}
