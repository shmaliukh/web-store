package com.vshmaliukh.webstore.controllers.handlers;

import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.items.CartItem;

import java.util.List;

public class ShoppingCartHandler {

    public int countAllItemsPrice(List<CartItem> cartItems){
        int totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice = totalPrice + cartItem.getItem().getSalePrice() * cartItem.getQuantity();
        }
        return totalPrice;
    }

    public int countAllItemsQuantity(List<CartItem> cartItems) {
        int totalCount = 0;
        for (CartItem cartItem : cartItems) {
            totalCount += cartItem.getQuantity();
        }
        return totalCount;
    }

}
