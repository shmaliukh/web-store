package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.carts.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByCartId(Long id);

    List<Cart> findCartsByUserId(Long id);

    List<Cart> findCartsByUserIdAndAuthorization(Long id,Boolean authorization);

}
