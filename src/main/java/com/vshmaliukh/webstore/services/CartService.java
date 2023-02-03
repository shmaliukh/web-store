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

    BaseCartRepository baseCartRepository;
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

    public void addItemToCart(Item item, Long cartId) {
        Cart cart = getCartByCartId(cartId).get();
        List<CartItem> cartItems = cart.getItems();
        if(cartItems.isEmpty()||!cartItems.stream().anyMatch(o->o.getItem().getId().equals(item.getId()))){
            CartItem cartItem = cartItemService.createNewCartItem(item, 1); // todo implement quantity checking
            cartItems.add(cartItem);
            cart.setItems(cartItems);
            addNewCart(cart);
        }
    }

    public Cart addNewCart(Cart cart){
        return cartRepositoryProvider.getCartRepositoryByCart(cart).save(cart);
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

    public Optional<Cart> getCartByCartId(Long id){
        return baseCartRepository.findById(id);
    }

}
