package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.carts.Cart;
import com.vshmaliukh.webstore.model.items.CartItem;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import com.vshmaliukh.webstore.repositories.CartItemRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartItemServiceTest {

    static final Book book = new Book(1, "book name", 1, 1, 1, 1, "some description", "some status", true, 0, 123, "some author", new Date());
    static final Magazine magazine = new Magazine(2, "magazine name", 2, 2, 2, 2, "some description", "some status", true, 0, 345);
    static final Newspaper newspaper = new Newspaper(3, "newspaper name", 3, 3, 3, 3, "some description", "some status", true, 0, 456);
    static final Comics comics = new Comics(4, "newspaper name", 4, 4, 4, 4, "some description", "some status", true, 0, 567, "some publisher");


    static final CartItem cartItemBook = new CartItem(1,book,1);
    static final CartItem cartItemMagazine = new CartItem(2,magazine,2);
    static final CartItem cartItemNewspaper = new CartItem(3,newspaper,2);
    static final CartItem cartItemComics = new CartItem(4, comics,3);

    @MockBean
    CartItemRepository cartItemRepository;

    @MockBean
    CartItemService cartItemService;


    private static Stream<Arguments> provideItemsForGettingCartItems() {
        return Stream.of(
                Arguments.of(comics, cartItemComics),
                Arguments.of(magazine,cartItemMagazine),
                Arguments.of(newspaper,cartItemNewspaper)
        );
    }

    @ParameterizedTest
    @MethodSource("provideItemsAndQuantityForCartItemCreation")
    void readCategoryByIdTest_failToFind(Item item,int quantityCartItem) {
        cartItemService.createNewCartItem(item,quantityCartItem);
        assertTrue(cartItemService.existsByItem(item));
    }

    @ParameterizedTest
    @MethodSource("provideItemsAndQuantityForCartItemCreation")
    void addCartItems(Item item, int quantity){
        CartItem cartItem = cartItemService.createNewCartItem(item,quantity);
        assertNotNull(cartItem);
        assertEquals(item, cartItem.getItem());
    }

    private static Stream<Arguments> provideItemsAndQuantityForCartItemCreation() {
        return Stream.of(
                Arguments.of(book,1),
                Arguments.of(magazine,2),
                Arguments.of(newspaper, 5)
        );
    }

}
