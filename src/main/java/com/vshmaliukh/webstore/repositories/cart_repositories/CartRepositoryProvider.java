package com.vshmaliukh.webstore.repositories.cart_repositories;

import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.carts.UserCart;
import com.vshmaliukh.webstore.repositories.UnauthorizedUserRepository;
import com.vshmaliukh.webstore.repositories.UserRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Component
public class CartRepositoryProvider {

    BaseCartRepository<UnauthorizedUserCart> unauthorizedUserCartRepository;

    BaseCartRepository<UserCart> userCartRepository;

    Map<String, BaseCartRepository> cartRepositoryMap;

    public CartRepositoryProvider(
            BaseCartRepository<UnauthorizedUserCart> unauthorizedUserCartRepository,
            BaseCartRepository<UserCart> userCartRepository) {
        this.unauthorizedUserCartRepository = unauthorizedUserCartRepository;
        this.userCartRepository = userCartRepository;
    }

    @PostConstruct
    private void postConstruct() {
        generateCartRepositoryMap();
    }
    public BaseCartRepository<? extends Cart> getCartRepositoryByUserAuthorization(boolean authorized) {
        if (authorized){
            return userCartRepository;
        }
        return unauthorizedUserCartRepository;
    }

    public <T extends Cart> BaseCartRepository<T> getCartRepositoryByCart(T cart) {
        return cartRepositoryMap.get(cart.getClass().getSimpleName());
    }

    private void generateCartRepositoryMap(){
        cartRepositoryMap = new HashMap<>();
        cartRepositoryMap.put(UnauthorizedUserCart.class.getSimpleName(),unauthorizedUserCartRepository);
        cartRepositoryMap.put(UserCart.class.getSimpleName(),userCartRepository);
    }

}
