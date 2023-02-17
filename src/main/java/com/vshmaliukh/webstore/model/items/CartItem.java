package com.vshmaliukh.webstore.model.items;

import com.vshmaliukh.webstore.model.carts.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id", nullable = false)
    private Long id;

    @OneToOne
    private Item item;

    @ManyToOne
//            TODO
    Cart cart;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public void setQuantity(int quantity) {

        // todo mb implement quantity check

        if(quantity>0){
            this.quantity = quantity;
        } else{
            this.quantity = 1;
        }
    }
}
