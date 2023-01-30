package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.carts.UserCart;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.CartItemRepository;
import com.vshmaliukh.webstore.repositories.UnauthorizedUserRepository;
import com.vshmaliukh.webstore.repositories.UserRepository;
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

    CartItemService cartItemService;
    CartItemRepository cartItemRepository;

    public void changeCartItemQuantityInCartOnOne(Integer cartItemId, Long userId, boolean authorized, boolean increment){
        Cart cart = getCartByUserId(userId,authorized);
        List<CartItem> cartItems = cart.getItems();
        for (CartItem cartItem : cartItems) {
            if(cartItem.getId().equals(cartItemId)){
                int resultQuantity;
                if(increment){
                    resultQuantity = cartItem.getQuantity()+1;
                } else {
                    resultQuantity = cartItem.getQuantity()-1;
                }
                if(resultQuantity<=cartItem.getItem().getAvailableToBuyQuantity()&&resultQuantity>0) {
                    cartItem.setQuantity(resultQuantity);cartItemRepository.save(cartItem);
                    cart.setItems(cartItems);
                    addNewCart(cart);
                    break;
                }
            }
        }
    }

    public void addItemToCart(Item item, Long userId, boolean authorized){
        // todo refactor its usage in other classes
        Cart cart = getCartByUserId(userId, authorized);
        if (cart!=null) {
            if(cartItemRepository.existsByItem(item)){
                changeCartItemQuantityInCartOnOne(cartItemRepository.getCartItemByItem(item).getId(),userId,authorized,true);
            } else {
                CartItem cartItem = cartItemService.createNewCartItem(item,1); // todo implement quantity checking
                List<CartItem> cartItems = cart.getItems();
                cartItems.add(cartItem);
                cart.setItems(cartItems);
                addNewCart(cart);
            }
        } else {
            CartItem cartItem = cartItemService.createNewCartItem(item,1);
            if(authorized){
                UserCart newCart = new UserCart();
                newCart.setItems(Collections.singletonList(cartItem));
                newCart.setUser(userRepository.getUserById(userId));
                addNewCart(newCart);
            } else {
                UnauthorizedUserCart newCart = new UnauthorizedUserCart();
                newCart.setItems(Collections.singletonList(cartItem));
                newCart.setUnauthorizedUser(unauthorizedUserRepository.getUnauthorizedUserById(userId));
                addNewCart(newCart);
            }
        }
    }

    public void addNewCart(Cart cart){
        cartRepositoryProvider.getCartRepositoryByCart(cart).save(cart);
    }

    public Cart getCartByUserId(Long userId, boolean authorization){
        if(authorization){
            User user = userRepository.getUserById(userId);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartByUser(user);
        } else {
            UnauthorizedUser user = unauthorizedUserRepository.getUnauthorizedUserById(userId);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartByUnauthorizedUser(user);
        }
    }

    public Cart getNewCartByAuthorization(boolean authorized){
        if(authorized) {
            return new UserCart();
        }
        return new UnauthorizedUserCart();
    }

}
