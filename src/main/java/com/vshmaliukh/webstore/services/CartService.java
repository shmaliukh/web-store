package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.carts.UserCart;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.UnauthorizedUserRepository;
import com.vshmaliukh.webstore.repositories.UserRepository;
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

    UserRepository userRepository;
    UnauthorizedUserRepository unauthorizedUserRepository;


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
        if(authorization){ // todo maybe create provider?
            User user = userRepository.getUserById(id);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartsByUser(user);
        } else {
            UnauthorizedUser user = unauthorizedUserRepository.getReferenceById(id);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartsByUnauthorizedUser(user);
        }
    }

    public Cart getCartByUserIdAndItemId(Long userId, Integer itemId, boolean authorization){
        List<? extends Cart> carts;
        if(authorization){ // todo maybe create provider?
            User user = userRepository.getUserById(userId);
            carts = cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartsByUser(user);
        } else {
            UnauthorizedUser user = unauthorizedUserRepository.getReferenceById(userId);
            carts = cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartsByUnauthorizedUser(user);
        }
        for (Cart cart : carts) {
            if(cart.getItem().getId()==itemId){
                return cart;
            }
        }
        return null; // todo fix null
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
