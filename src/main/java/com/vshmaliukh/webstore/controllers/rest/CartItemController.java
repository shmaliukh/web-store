package com.vshmaliukh.webstore.controllers.rest;

import com.vshmaliukh.webstore.controllers.handlers.ShoppingCartHandler;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/cart-item")
public class CartItemController {

    final ShoppingCartHandler shoppingCartHandler;

    final CartService cartService;

    @PutMapping("/add-one/{category}/{type}/{cartItemId}")
    public ResponseEntity<CartItem> incItemQuantity(@PathVariable String category,
                                                    @PathVariable String type, // todo mb optimize it
                                                    @PathVariable Long cartItemId,
                                                    @CookieValue Long userId) {
        // todo add checking authorization
        boolean authorization = false;
        CartItem changedCartItem = cartService.changeCartItemQuantityInCartOnOne(cartItemId,userId,authorization,true); // because of incrementing
        return ResponseEntity.ok(changedCartItem);
    }

    @PutMapping("/remove-one/{category}/{type}/{cartItemId}")
    public ResponseEntity<CartItem> decItemQuantity(@PathVariable String category,
                                                    @PathVariable String type, // todo mb optimize it
                                                    @PathVariable Long cartItemId,
                                                    @CookieValue Long userId) {
        // todo implement authorization checking
        boolean authorization = false;
        CartItem changedCartItem = cartService.changeCartItemQuantityInCartOnOne(cartItemId,userId,authorization,false); // because of decrementing
        return ResponseEntity.ok(changedCartItem);
    }

    @PutMapping("/remove-all/{cartItemId}")
    public ResponseEntity<CartItem> removeItemsType(@PathVariable Long cartItemId,
                                                    @CookieValue Long cartId) {
        CartItem changedCartItem = shoppingCartHandler.removeCartItemFromCart(cartId,cartItemId);
        return ResponseEntity.ok(changedCartItem);
    }

    @DeleteMapping("/remove-all-items")
    public ResponseEntity<List<CartItem>> removeAllItemsFromCart(@CookieValue Long cartId) {
        return ResponseEntity.ok(
                cartService
                        .removeAllItemsFromCart(cartId)
                        .getItems());
    }

    @PostMapping("/add-new/{category}/{type}/{itemId}")
    public ResponseEntity<CartItem> addItemToCartFromMainPage(@PathVariable String category,
                                                              @PathVariable String type,
                                                              @PathVariable Integer itemId,
                                                              @CookieValue(defaultValue = "0") Long cartId,
                                                              HttpServletResponse response) {
        // todo add checking user authorization
        boolean authorization = false;
        CartItem cartItem = shoppingCartHandler.addItemToCartFromMainPage(authorization,cartId,itemId,type,response);
        if (cartItem!=null){
            return ResponseEntity.ok(cartItem);
        }
        return ResponseEntity.notFound().build();
    }

}
