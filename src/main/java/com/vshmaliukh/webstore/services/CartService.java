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


    public void addItemToCart(Item item, Long userId, boolean authorized){

        // todo refactor its usage in other classes
        Cart cart = getCartByUserId(userId, authorized);

        if (cart!=null) {
            for (CartItem cartItemToFound : cart.getItems()) {
                if(Objects.equals(cartItemToFound.getItem().getId(), item.getId())){
                    int resultQuantity = cartItemToFound.getQuantity()+1; // todo mb implement method for item availability checking
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

    // todo implement data migration

    public Cart getCartByUserId(Long id, Boolean authorization){ // todo change list to one cart
        if(authorization){
            User user = userRepository.getUserById(id);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartByUser(user);
        } else {
            UnauthorizedUser user = unauthorizedUserRepository.getReferenceById(id);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartByUnauthorizedUser(user);
        }
    }

    public Cart getCartByUserId(Long userId, boolean authorization){
        if(authorization){
            User user = userRepository.getUserById(userId);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartByUser(user);
        } else {
            UnauthorizedUser user = unauthorizedUserRepository.getReferenceById(userId);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartByUnauthorizedUser(user);
        }
    }



    public void decItemQuantityInCart(Item item, Long userId, boolean authorized){
        Cart cart = getCartByUserId(userId,authorized);
        if (cart!=null) {
            for (CartItem cartItem : cart.getItems()) {
                Item resultItem = cartItem.getItem();
                if(Objects.equals(resultItem.getId(), item.getId())){
                    cartItem.setQuantity(cartItem.getQuantity()-1);
                    long resultQuantity = cartItem.getQuantity();
                    if(resultQuantity<=0){
                        cart.getItems().remove(cartItem); // todo check this method
                    }
                }
            }
            addNewCart(cart);
        }
    }

    public Cart getNewCartByAuthorization(boolean authorized){
        if(authorized) {
            return new UserCart();
        }
        return new UnauthorizedUserCart();
    }

}
