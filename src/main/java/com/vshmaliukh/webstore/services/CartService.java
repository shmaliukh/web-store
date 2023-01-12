package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.carts.UserCart;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.cart_repositories.BaseCartRepository;
import com.vshmaliukh.webstore.repositories.cart_repositories.CartRepositoryProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CartService {

    final CartRepositoryProvider cartRepositoryProvider;

    public void addItemToCart(Item item, Long userId, boolean authorized){
        Cart cart = getCartByUserIdAndItemId(userId, item.getId(), authorized);

        if (cart!=null) {
            cart.setItemQuantity(cart.getItemQuantity() + 1);
        } else {

            // todo implement method (?) for creating new cart


            Cart newCart = getNewCartByAuthorization(authorized);
//            newCart.setAuthorization(authorization);
//            newCart.setItemId(item.getId());
//            newCart.setItemQuantity(item.getQuantity());
//            newCart.setPrice(item.getPrice());
            addNewCart(newCart);
        }
    }

    public void addNewCart(Cart cart){
        BaseCartRepository repository = cartRepositoryProvider.getCartRepositoryByCart(cart);
        repository.save(cart);
    }

    public List<? extends Cart> getCartsByUserId(Long id, Boolean authorization){
        return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartsByUserId(id);
    }

    public Cart getCartByUserIdAndItemId(Long userId, Integer itemId, Boolean authorization){
        return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartByUserIdAndItemId(userId,itemId);
    }

    public void decItemQuantityInCart(Item item, Long userId, boolean authorized){
        Cart cart = getCartByUserIdAndItemId(userId, item.getId(), authorized);
        if (cart!=null) {
            cart.setItemQuantity(cart.getItemQuantity() - 1);
        }
    }

    public void removeItemFromCart(Item item, Long userId, boolean authorized){
        BaseCartRepository repository = cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorized);
        repository.delete(getCartByUserIdAndItemId(userId, item.getId(),authorized));
    }

    public Cart getNewCartByAuthorization(boolean authorized){
        if(authorized) {
            return new UserCart();
        }
        return new UnauthorizedUserCart();
    }

}
