package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.items.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}
