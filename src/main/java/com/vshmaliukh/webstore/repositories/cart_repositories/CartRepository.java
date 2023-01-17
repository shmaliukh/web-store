package com.vshmaliukh.webstore.repositories.cart_repositories;

import com.vshmaliukh.webstore.model.carts.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByCartId(Long id);


}