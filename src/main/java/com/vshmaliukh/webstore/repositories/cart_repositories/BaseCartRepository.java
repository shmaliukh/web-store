package com.vshmaliukh.webstore.repositories.cart_repositories;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.carts.UserCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseCartRepository<T extends Cart> extends JpaRepository<T,Long> {

    UserCart findCartByUser(User user);

    UnauthorizedUserCart findCartByUnauthorizedUser(UnauthorizedUser user);


}
