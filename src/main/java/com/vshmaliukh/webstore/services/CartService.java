package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Cart;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.CartRepository;
import com.vshmaliukh.webstore.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CartService {

    final CartRepository cartRepository;
    final UserRepository userRepository;

    void addItemToCart(Item item, String userName){
        Long userId = userRepository.getUserByUsername(userName).getId();
        List<Cart> carts = getCartsByUserId(userId);
        boolean found = false;
        for (Cart cart : carts) {
            if (Objects.equals(cart.getItemId(), item.getId())) {
                cart.setItemQuantity(cart.getItemQuantity() + 1);
                found = true;
                break;
            }
        }
        if(!found){
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setItemId(item.getId());
            newCart.setItemQuantity(item.getQuantity());
            newCart.setCategory(item.getCategory());
            cartRepository.save(newCart);
        } else {
            cartRepository.saveAll(carts);
        }
    }

    List<Cart> getCartsByUserId(Long id){
        return cartRepository.findCartsByUserId(id);
    }

}
