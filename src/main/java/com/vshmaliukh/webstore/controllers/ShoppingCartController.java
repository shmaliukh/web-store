package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.Cart;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import com.vshmaliukh.webstore.services.CartService;
import com.vshmaliukh.webstore.services.ItemService;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.SHOPPING_CART;
import static com.vshmaliukh.webstore.controllers.ViewsNames.SHOPPING_CART_VIEW;

@Controller
@RequestMapping("/" + SHOPPING_CART)
@AllArgsConstructor
public class ShoppingCartController {

    final ItemService itemService;
    final UserService userService;
    final CartService cartService;
    final ItemRepositoryProvider itemRepositoryProvider;

    @GetMapping
    public ModelAndView showCartPage(ModelMap modelMap){
        List<Item> items = getTestItemOrderList();
        modelMap.addAttribute("items",items); // todo implement items adding to template
        modelMap.addAttribute("totalItems",items.size());


//        List<Cart> carts = cartService.getCartsByUserId(userService.readUserIdByName("")); // todo add username usage

        int totalPrice = 0;

        for (Item item : items) {  // for tests
            totalPrice = totalPrice + item.getPrice();
        }


        modelMap.addAttribute("totalPrice",totalPrice);
        return new ModelAndView(SHOPPING_CART_VIEW);
    }

    @PostMapping("/add-one/{type}/{id}")
    public String incItemQuantity(@PathVariable String type,
                                        @PathVariable Integer id){

        BaseItemRepository itemRepository = itemRepositoryProvider.getItemRepositoryByItemClassName(type);
        Item item = itemRepository.getById(id);
        cartService.addItemToCart(item,"username"); // todo implement username usage
        return "redirect:/" + SHOPPING_CART;
    }

    @PostMapping("/remove-one/{type}/{id}")
    public String decItemQuantity(@PathVariable String type,
                                  @PathVariable Integer id){
        BaseItemRepository itemRepository = itemRepositoryProvider.getItemRepositoryByItemClassName(type);
        Item item = itemRepository.getById(id);
        cartService.decItemQuantityInCart(item,"username"); // todo implement username usage
        return "redirect:/" + SHOPPING_CART;
    }

    public List<Cart> getTestCarts(){
        List<Cart> carts = new ArrayList<>();

        Cart first = new Cart();
        first.setCartId(Long.getLong("1"));
        first.setUserId(Long.getLong("1"));
        first.setCategory("book");
        first.setItemQuantity(4);
        first.setPrice(1000);

        Cart second = new Cart();
        second.setCartId(Long.getLong("1"));
        second.setUserId(Long.getLong("1"));
        second.setCategory("book");
        second.setItemQuantity(4);
        second.setPrice(1000);
        carts.add(first);
        carts.add(second);
        return carts;
    }

    private static List<Item> getTestItemOrderList() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Book(1, "1 book name", "book", 2, 3, true, 4, "Vlad1", new Date()));
        itemList.add(new Book(2, "2 book name", "book", 3, 4, true, 5, "Vlad2", new Date()));
        itemList.add(new Magazine(3, "Magazine name", "magazine", 4, 5, true, 6));
        itemList.add(new Comics(4, "Comics name", "comics", 5, 6, true, 7, "Some publisher"));
        return itemList;
    }

}
