package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.carts.UnauthorizedUserCart;
import com.vshmaliukh.webstore.model.carts.UserCart;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import com.vshmaliukh.webstore.repositories.CartItemRepository;
import com.vshmaliukh.webstore.repositories.cart_repositories.BaseCartRepository;
import com.vshmaliukh.webstore.repositories.cart_repositories.CartRepository;
import com.vshmaliukh.webstore.repositories.cart_repositories.CartRepositoryProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @MockBean
    CartRepositoryProvider cartRepositoryProvider;

    @MockBean
    CartRepository cartRepository;

    @MockBean
    CartItemRepository cartItemRepository;

    @MockBean
    BaseCartRepository baseCartRepository;

    @Autowired
    CartService cartService;



    static final Magazine magazine = new Magazine(2, "magazine name", 2, 2, 2, 2, "some description", "some status", true, 0, 345);
    static final Newspaper newspaper = new Newspaper(3, "newspaper name", 3, 3, 3, 3, "some description", "some status", true, 0, 456);
    static final Comics comics = new Comics(4, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 0, 567, "some publisher");

    static final CartItem cartItemNewspaper = new CartItem(3L, newspaper,new UnauthorizedUserCart(),3);
    static final CartItem cartItemComics = new CartItem(4L,comics, new UnauthorizedUserCart(),4);
    static final CartItem cartItemMagazine = new CartItem(2L,magazine, new UnauthorizedUserCart(),1);

    final static Cart userCart = new UserCart(new User());
    final static Cart unauthorizedUserCart = new UnauthorizedUserCart(new UnauthorizedUser(1L));

    @ParameterizedTest
    @MethodSource("provideItemsForTrueExistenceChecking")
    void existsCartByIdPositiveTest(Item item, boolean exists) {

//
//        cartService.getCartByUserId();
    }

    private static Stream<Arguments> provideOptionalCartsForGetCartById() {
        return Stream.of(
                Arguments.of(1L, Optional.of(userCart)),
                Arguments.of(2L, Optional.of(unauthorizedUserCart))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOptionalCartsForGetCartById")
    void getCartByIdTest(Long id, Optional<Cart> expectedCart) {
        Cart cart = expectedCart.get();
        cart.setCartId(id);
        expectedCart = Optional.of(cart);
        Mockito.when(cartRepository.findByCartId(id)).thenReturn(expectedCart);
        Optional<Cart> optionalCart = cartService.getCartByCartId(id);
        assertNotNull(optionalCart);
        assertTrue(optionalCart.isPresent());
        assertEquals(expectedCart.get(),optionalCart.get());
    }

    private static Stream<Arguments> provideCartIdForExistenceChecking() {
        return Stream.of(
                Arguments.of(1L, true, true),
                Arguments.of(2L, false, true),
                Arguments.of(0L,false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCartIdForExistenceChecking")
    void existsCartByIdPositiveTest(Long id, boolean authorization, boolean exists) {
        Mockito.when(cartRepositoryProvider.getCartRepositoryByUserAuthorization(authorization)).thenReturn(baseCartRepository);
        Mockito.when(baseCartRepository.existsById(id)).thenReturn(exists);
        assertEquals(exists,cartService.existsById(id,authorization));
    }

    private static Stream<Arguments> provideCartsForRemovingAllItems() {
        return Stream.of(
                Arguments.of(userCart,1L, Arrays.asList(new CartItem(),new CartItem())),
                Arguments.of(unauthorizedUserCart,2L, Collections.emptyList())
        );
    }

    @ParameterizedTest
    @MethodSource("provideCartsForRemovingAllItems")
    void removeAllItemsFromCartTest(Cart expectedCart, Long cartId, List<CartItem> cartItems) {
        expectedCart=generateCartForTests(expectedCart,cartId,cartItems);
        Optional<Cart> optionalCart = Optional.of(expectedCart);
        Mockito.when(cartRepositoryProvider.getCartRepositoryByCart(expectedCart)).thenReturn(baseCartRepository);
        Mockito.when(cartRepository.findByCartId(cartId)).thenReturn(optionalCart);
        expectedCart.setItems(new ArrayList<>());
        Mockito.when(baseCartRepository.save(expectedCart)).thenReturn(expectedCart);
        assertEquals(expectedCart,cartService.removeAllItemsFromCart(cartId));
    }

    Cart generateCartForTests(Cart cart, Long cartId, List<CartItem> cartItems){
        cart.setCartId(cartId);
        cart.setItems(cartItems);
        return cart;
    }

    private static Stream<Arguments> provideCartsForSave() {
        return Stream.of(
                Arguments.of(userCart),
                Arguments.of(unauthorizedUserCart)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCartsForSave")
    void addNewCartTest(Cart expectedCart) {
        Mockito.when(cartRepositoryProvider.getCartRepositoryByCart(expectedCart)).thenReturn(baseCartRepository);
        Mockito.when(baseCartRepository.save(expectedCart)).thenReturn(expectedCart);
        assertEquals(expectedCart,cartService.addNewCart(expectedCart));
    }

    private static Stream<Arguments> provideCartsForRemoveCartItem() {
        return Stream.of(
                Arguments.of(userCart,
                        new ArrayList<>(Arrays.asList(cartItemComics,cartItemMagazine,cartItemNewspaper)),
                        new ArrayList<>(Arrays.asList(cartItemComics,cartItemNewspaper)),
                        2L),
                Arguments.of(unauthorizedUserCart,
                        new ArrayList<>(Collections.singletonList(cartItemNewspaper)),
                        new ArrayList<>(),
                        3L),
                Arguments.of(unauthorizedUserCart,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        3L),
                Arguments.of(unauthorizedUserCart,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        0L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCartsForRemoveCartItem")
    void removeOneCartItemsTypeFromCartTest(Cart cart, List<CartItem> cartItems, List<CartItem> expectedCartItems, Long cartItemId) {
        cart = generateCartForTests(cart,1L,cartItems);
        Mockito.when(cartRepositoryProvider.getCartRepositoryByCart(cart)).thenReturn(baseCartRepository);
        assertTrue(expectedCartItems.containsAll(cartService.removeItemFromCart(cart,cartItemId).getItems()));
    }

//    private static Stream<Arguments> provideCartsForRemoveCart() {
//        return Stream.of(
//                Arguments.of(userCart,
//                        new ArrayList<>(Arrays.asList(cartItemComics,cartItemMagazine,cartItemNewspaper)),
//                        new ArrayList<>(Arrays.asList(cartItemComics,cartItemNewspaper)),
//                        2)
//        );
//    }

//    @ParameterizedTest   // todo create tests
//    @MethodSource("provideCartsForRemoveCart")
//    void changeCartItemQuantityInCartOnOneIncrementTest(Integer cartItemId, Long userId, boolean authorized) {
//
//
//        cartService.changeCartItemQuantityInCartOnOne(cartItemId,userId,authorized,true);
//
//
//
//    }

}
