package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.carts.UserCart;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.UnauthorizedUserRepository;
import com.vshmaliukh.webstore.repositories.UserRepository;
import com.vshmaliukh.webstore.repositories.cart_repositories.BaseCartRepository;
import com.vshmaliukh.webstore.repositories.cart_repositories.CartRepositoryProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class CartService {

    final CartRepositoryProvider cartRepositoryProvider;

    UserRepository userRepository;
    UnauthorizedUserRepository unauthorizedUserRepository;


    public void addItemsToCart(Item item, Long userId, boolean authorized, int quantity){

        // todo refactor its usage in other classes
        Cart cart = getCartByUserId(userId, authorized);

        if (cart!=null) {
            for (CartItem cartItemToFound : cart.getItems()) {
                if(Objects.equals(cartItemToFound.getItem().getId(), item.getId())){
                    int resultQuantity = cartItemToFound.getQuantity()+quantity; // todo mb implement item availability checking
                    if(resultQuantity<=cartItemToFound.getItem().getAvailableToBuyQuantity()){
                        cartItemToFound.setQuantity(resultQuantity);
                    }
                }
            }
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setItem(item);
            cartItem.setQuantity(quantity);
            if(authorized){
                UserCart newCart = new UserCart();
                newCart.setItems(Collections.singletonList(cartItem));
                newCart.setPrice(item.getSalePrice()*quantity);
                newCart.setUser(userRepository.getUserById(userId));
                addNewCart(newCart);
            } else {
                UnauthorizedUserCart newCart = new UnauthorizedUserCart();
                newCart.setItems(Collections.singletonList(cartItem));
                newCart.setPrice(item.getSalePrice()*quantity);
                newCart.setUnauthorizedUser(unauthorizedUserRepository.getUnauthorizedUserById(userId));
                addNewCart(newCart);
            }
        }
    }

    public void addNewCart(Cart cart){
        BaseCartRepository repository = cartRepositoryProvider.getCartRepositoryByCart(cart);
        repository.save(cart);
    }

    // todo implement migration

    public List<? extends Cart> getCartsByUserId(Long id, Boolean authorization){ // todo change list to one cart
        if(authorization){ // todo maybe create provider?
            User user = userRepository.getUserById(id);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartsByUser(user);
        } else {
            UnauthorizedUser user = unauthorizedUserRepository.getReferenceById(id);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartsByUnauthorizedUser(user);
        }
    }

    public Cart getCartByUserId(Long userId, boolean authorization){
        List<? extends Cart> carts;
        if(authorization){ // todo maybe create provider?
            User user = userRepository.getUserById(userId);
            carts = cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartsByUser(user);
        } else {
            UnauthorizedUser user = unauthorizedUserRepository.getReferenceById(userId);
            carts = cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartsByUnauthorizedUser(user);
        }
//        for (Cart cart : carts) {
//            if(cart.getItem().getId()==itemId){ // todo refactor
//                return cart;
//            }
//        }
        return null; // todo fix null
    }



    public void decItemQuantityInCart(Item item, Long userId, boolean authorized){
        Cart cart = getCartByUserId(userId, item.getId(), authorized);
        if (cart!=null) {
            cart.setItemQuantity(cart.getItemQuantity() - 1);
        }
    }

    public void removeItemFromCart(Item item, Long userId, boolean authorized){
        BaseCartRepository repository = cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorized);
        repository.delete(getCartByUserId(userId, item.getId(),authorized));
    }

    public Cart getNewCartByAuthorization(boolean authorized){
        if(authorized) {
            return new UserCart();
        }
        return new UnauthorizedUserCart();
    }

}
