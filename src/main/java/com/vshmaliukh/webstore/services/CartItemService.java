package com.vshmaliukh.webstore.services;


import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.CartItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartItemService {

    final CartItemRepository cartItemRepository;

    public CartItem createNewCartItem(Item item, int quantity){
        CartItem cartItem = new CartItem();
        cartItem.setItem(item);
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return cartItem;
    }

    public CartItem getCartItemByItem(Item item){
        return cartItemRepository.getCartItemByItem(item);
    }

    public boolean existsByItem(Item item){
        return cartItemRepository.existsByItem(item);
    }

    public Optional<CartItem> readCartItemById(Long cartId) {
        return cartItemRepository.readById(cartId);
    }

    public CartItem saveCartItem(CartItem cartItem){
        return cartItemRepository.save(cartItem);
    }

    public void removeCartItem(CartItem cartItem){
        cartItemRepository.delete(cartItem);
    }

    public void removeAllCartItems(List<CartItem> cartItems){
        cartItemRepository.deleteAll(cartItems);
    }

}
