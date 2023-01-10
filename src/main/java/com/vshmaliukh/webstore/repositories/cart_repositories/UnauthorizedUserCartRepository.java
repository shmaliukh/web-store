package com.vshmaliukh.webstore.repositories.cart_repositories;

import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UnauthorizedUserCartRepository extends JpaRepository<UnauthorizedUserCart, Long> {



}
