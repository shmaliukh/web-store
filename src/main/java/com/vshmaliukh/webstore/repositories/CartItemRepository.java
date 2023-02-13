package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.model.items.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

    Optional<CartItem> readById(Long integer);

    boolean existsByItem(Item item);

    CartItem getCartItemByItem(Item item);

}
