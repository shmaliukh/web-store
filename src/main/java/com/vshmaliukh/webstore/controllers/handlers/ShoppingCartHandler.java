package com.vshmaliukh.webstore.controllers.handlers;

import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.carts.UserCart;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.cart_repositories.UserCartRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import com.vshmaliukh.webstore.services.CartItemService;
import com.vshmaliukh.webstore.services.CartService;
import com.vshmaliukh.webstore.services.UnauthorizedUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ShoppingCartHandler {

    CookieHandler cookieHandler;
    CartService cartService;
    CartItemService cartItemService;
    UnauthorizedUserService unauthorizedUserService;
    UserCartRepository userCartRepository;
    ItemRepositoryProvider itemRepositoryProvider;

    public void changeCartToAuthorized(Cart unauthorizedUserCart, User authorizedUser){
        UserCart userCart = new UserCart();
        userCart.setUser(authorizedUser);
        userCart.setItems(unauthorizedUserCart.getItems());
        userCartRepository.save(userCart);
    }

    public Cart createNewCart(){
        UnauthorizedUserCart unauthorizedUserCart = new UnauthorizedUserCart();
        unauthorizedUserCart.setUnauthorizedUser(unauthorizedUserService.createUnauthorizedUser());
        return cartService.addNewCart(unauthorizedUserCart);
    }

    public List<CartItem> showShoppingCart(boolean authorization, Long cartId, HttpServletResponse response){
        List<CartItem> cartItems = new ArrayList<>();
        if(!authorization){
            if(cartId==null){
                cartId = createNewCart().getCartId();
                response.addCookie(
                        cookieHandler.createCookie(cartId,"cartId")
                );
            }
            Optional<Cart> optionalCart = cartService.getCartByCartId(cartId);

            if (optionalCart.isPresent()){
                Cart cart = optionalCart.get();
                 cartItems = cart.getItems();
            }
        }
        return cartItems;
    }

    public void addItemToCartFromMainPage(boolean authorization, Long cartId, Integer itemId, String type,  HttpServletResponse response){
        if(!authorization){
            if(cartId==0||!cartService.existsById(cartId,authorization)){
                cartId = createNewCart().getCartId();
                Long userId = cartService.getCartByCartId(cartId).get().getCartId(); // todo remove userId usage
                response.addCookie(new CookieHandler().createCookie(cartId,"cartId"));
                response.addCookie(new CookieHandler().createCookie(userId,"userId"));
            }
        }
        BaseItemRepository itemRepository = itemRepositoryProvider.getItemRepositoryByItemClassName(type);
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        cartService.addItemToCart(optionalItem.get(), cartId);
    }

    public CartItem removeCartItemFromCart(Long cartId, Long cartItemId){
        Optional<Cart> optionalCart = cartService.getCartByCartId(cartId);
        if(optionalCart.isPresent()){
            cartService.removeOneCartItemsTypeFromCart(optionalCart.get(), cartItemId);
            Optional<CartItem> optionalCartItem = cartItemService.readCartItemById(cartItemId);
            if(optionalCartItem.isPresent()){
                return optionalCartItem.get();
            }
        }
        return null;
    }

}
