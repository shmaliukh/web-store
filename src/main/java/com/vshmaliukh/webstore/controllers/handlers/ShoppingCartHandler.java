package com.vshmaliukh.webstore.controllers.handlers;

import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.carts.UserCart;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.repositories.cart_repositories.UserCartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ShoppingCartHandler {

    UserCartRepository userCartRepository;

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

    public void changeCartToAuthorized(Cart unauthorizedUserCart, User authorizedUser){
        UserCart userCart = new UserCart();
        userCart.setUser(authorizedUser);
        userCart.setItems(unauthorizedUserCart.getItems());
        userCartRepository.save(userCart);
    }

}
