package com.vshmaliukh.webstore.repositories.cart_repositories;

import com.vshmaliukh.webstore.model.carts.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseCartRepository<T extends Cart> extends JpaRepository<T,Long> {

    List<T> findCartsByUserId(Long id);

}
