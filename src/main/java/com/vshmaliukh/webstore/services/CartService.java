package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.cart_repositories.CartRepository;
import com.vshmaliukh.webstore.repositories.UserRepository;
import com.vshmaliukh.webstore.repositories.cart_repositories.CartRepositoryProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CartService {

    final CartRepositoryProvider cartRepositoryProvider;

    public void addOneItemToCart(Item item, Long userId, boolean authorization){
        List<Cart> carts = getCartsByUserId(userId,authorization);
    }

    public List<Cart> getCartsByUserId(Long id, Boolean authorization){

        List<Cart> carts = cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization).findCartsByUserId(id);

        return carts.stream().filter(cart->cart.isAuthorization()==authorization).collect(Collectors.toList());
    }

//    final CartRepository cartRepository;
//    final UserRepository userRepository;
//
//    public void addItemToCart(Item item, Long userId, boolean authorization){
//        List<Cart> carts = getCartsByUserId(userId,authorization);
//        boolean found = false;
//        for (Cart cart : carts) {
//            if (Objects.equals(cart.getItemId(), item.getId())&&Objects.equals(cart.getCategory(),item.getCategory())) {
//                cart.setItemQuantity(cart.getItemQuantity() + 1);
//                found = true;
//                break;
//            }
//        }
//        if(!found){
//            Cart newCart = new Cart();
//            newCart.setUserId(userId);
//            newCart.setAuthorization(authorization);
//            newCart.setItemId(item.getId());
//            newCart.setItemQuantity(item.getQuantity());
//            newCart.setCategory(item.getCategory());
//            newCart.setPrice(item.getPrice());
//            cartRepository.save(newCart);
//        } else {
//            cartRepository.saveAll(carts);
//        }
//    }
//
//    public void removeItemFromCart(Item item, Long userId, boolean authorization){
//        List<Cart> carts = getCartsByUserId(userId,authorization);
//        for (Cart cart : carts) {
//            if (Objects.equals(cart.getItemId(),item.getId())&&Objects.equals(cart.getCategory(),item.getCategory())) {
//                cartRepository.delete(cart);
//                break;
//            }
//        }
//    }
//
//    public void decItemQuantityInCart(Item item, Long userId, boolean authorization){
//        List<Cart> carts = getCartsByUserId(userId,authorization);
//        for (Cart cart : carts) {
//            if (Objects.equals(cart.getItemId(),item.getId())&&Objects.equals(cart.getCategory(),item.getCategory())) {
//                cart.setItemQuantity(cart.getItemQuantity()-1);
//                if(cart.getItemQuantity()==0){
//                    cartRepository.delete(cart);
//                }
//                break;
//            }
//        }
//    }
//
//    public List<Cart> getCartsByUserId(Long id, Boolean authorization){
//
//        List<Cart> carts = cartRepository.findCartsByUserId(id);
//
//        return carts.stream().filter(cart->cart.isAuthorization()==authorization).collect(Collectors.toList());
//    }

}
