package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.ItemUtil;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ActionsWithItemRepositoryProvider;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.*;

@Slf4j
@Controller
@RequestMapping("/" + ADD_ITEM_PAGE)
@AllArgsConstructor
public class AddItemController {

    public static final String ITEM_TYPE = "itemType";

    final UserService userService;
    final ActionsWithItemRepositoryProvider itemRepositoryProvider;

    @GetMapping("/{" + ITEM_TYPE + "}")
    public ModelAndView doGet(@PathVariable(ITEM_TYPE) String itemType,
                              @CookieValue(defaultValue = "0") Long userId,
                              ModelMap modelMap) {
        boolean itemTypeExist = ItemUtil.itemNameList.stream().anyMatch(itemType::equalsIgnoreCase);
        if (itemTypeExist) {
            modelMap.addAttribute(ITEM_TYPE, itemType);
            boolean isAdminUser = userService.isAdminUser(userId);
            if (
                    ! // TODO remove '!'
                            isAdminUser) {
                return new ModelAndView(ADD_ITEM_PAGE, modelMap);
            }
        }
        return new ModelAndView("redirect:/" + HOME_PAGE, modelMap);
    }

    @PutMapping("/add_item_to_db")
    <T extends Item> ResponseEntity<Void> addItem(@CookieValue(defaultValue = "0") Long userId,
                                                  @RequestBody T item) {

//        .addItem(item);
//        read items
//        List<Item> itemListAfterAction = ;
//        if (itemListAfterAction.contains(item)) {
//            return ResponseEntity.ok().build();
//        }
        log.warn("userId: '{}' // item '{}' not added to database", userId, item);
        return ResponseEntity.badRequest().build();
    }

}
