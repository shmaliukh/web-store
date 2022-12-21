package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByCartId(Long id);

}
