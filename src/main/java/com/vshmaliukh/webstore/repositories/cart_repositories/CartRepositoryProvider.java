package com.vshmaliukh.webstore.repositories.cart_repositories;

import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.carts.UserCart;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class CartRepositoryProvider {

    BaseCartRepository<UnauthorizedUserCart> unauthorizedUserCartRepository;

    BaseCartRepository<UserCart> userCartRepository;

    Map<Cart, BaseCartRepository> cartRepositoryMap;

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
        return cartRepositoryMap.get(cart);
    }

    private void generateCartRepositoryMap(){
        cartRepositoryMap = new HashMap<>();
        cartRepositoryMap.put(new UnauthorizedUserCart(),unauthorizedUserCartRepository);
        cartRepositoryMap.put(new UserCart(),userCartRepository);
    }

}
