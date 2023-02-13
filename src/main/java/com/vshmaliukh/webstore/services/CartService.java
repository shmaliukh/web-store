package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import com.vshmaliukh.webstore.repositories.CartItemRepository;
import com.vshmaliukh.webstore.repositories.UnauthorizedUserRepository;
import com.vshmaliukh.webstore.repositories.UserRepository;
import com.vshmaliukh.webstore.repositories.cart_repositories.CartRepository;
import com.vshmaliukh.webstore.repositories.cart_repositories.CartRepositoryProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class CartService {

    final CartRepositoryProvider cartRepositoryProvider;
    CartRepository cartRepository;
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
            User user = userRepository.getReferenceById(userId);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartByUser(user);
        } else {
            UnauthorizedUser user = unauthorizedUserRepository.getUnauthorizedUserById(userId);
            return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartByUnauthorizedUser(user);
        }
    }

    public Optional<Cart> getCartByCartId(Long id){
        return cartRepository.findByCartId(id);
    }

    public Cart removeOneCartItemsTypeFromCart(Cart cart, Integer cartItemId){
        return cartRepositoryProvider
                .getCartRepositoryByCart(cart)
                .save(removeItemFromCart(cart,cartItemId));
    }

    Cart removeItemFromCart(Cart cart, Integer cartItemId){
        List<CartItem> cartItems = cart.getItems();
        CartItem cartItemToRemove = new CartItem();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getId().equals(cartItemId)){
                cartItemToRemove = cartItem;
                cartItems.remove(cartItem);
                break;
            }
        }
        cart.setItems(cartItems);
        if(cartItemToRemove.getId()!=null){
            cartItemRepository.delete(cartItemToRemove);
        }
        return cart;
    }

    public Cart removeAllItemsFromCart(Long cartId){
        Cart cart = getCartByCartId(cartId).get();
        List<CartItem> newCartItems = new ArrayList<>();
        List<CartItem> cartItems = cart.getItems();
        cart.setItems(newCartItems);
        cart = addNewCart(cart);
        cartItemRepository.deleteAll(cartItems);
        return cart;
    }

    public boolean existsById(Long id, boolean authorization){
        return cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).existsById(id);
    }

}