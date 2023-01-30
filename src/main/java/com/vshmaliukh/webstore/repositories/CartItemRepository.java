package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.model.items.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

    boolean existsByItem(Item item);

    CartItem getCartItemByItem(Item item);

}
