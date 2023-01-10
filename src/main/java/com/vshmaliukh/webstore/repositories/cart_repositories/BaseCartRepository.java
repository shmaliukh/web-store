package com.vshmaliukh.webstore.repositories.cart_repositories;

import com.vshmaliukh.webstore.model.carts.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseCartRepository<T extends Cart> extends JpaRepository<T,Long> {

}
