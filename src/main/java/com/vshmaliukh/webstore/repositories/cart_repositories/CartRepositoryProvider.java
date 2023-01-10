package com.vshmaliukh.webstore.repositories.cart_repositories;

import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.carts.UserCart;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CartRepositoryProvider {

    BaseCartRepository<UnauthorizedUserCart> unauthorizedUserCartRepository;

    BaseCartRepository<UserCart> userCartRepository;

    public CartRepositoryProvider(
            BaseCartRepository<UnauthorizedUserCart> unauthorizedUserCartRepository,
            BaseCartRepository<UserCart> userCartRepository) {
        this.unauthorizedUserCartRepository = unauthorizedUserCartRepository;
        this.userCartRepository = userCartRepository;
    }

    public BaseCartRepository<? extends Cart> getCartRepositoryByUserAuthorization(boolean authorized) {
        if (authorized){
            return userCartRepository;
        }
        return unauthorizedUserCartRepository;
    }

}
