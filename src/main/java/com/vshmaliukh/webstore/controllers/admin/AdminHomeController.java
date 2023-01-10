package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminHomeController {
//  TODO refactor 'home' page

    final UserService userService;
    final ItemRepositoryProvider itemRepositoryProvider;

    @GetMapping("/**")
    public ModelAndView doGet(@CookieValue(defaultValue = "0") Long userId,
                              ModelMap modelMap) {
        long allItemTypesQuantity = calcAllItem();
        Map<String, Long> categoryItemQuantityMap = Collections.singletonMap("Literature", allItemTypesQuantity);
        modelMap.addAttribute("categoryItemQuantityMap", categoryItemQuantityMap);
        boolean isAdminUser = userService.isAdminUser(userId);
        if (
                ! // TODO remove '!'
                        isAdminUser) {
            return new ModelAndView("admin/admin-home", modelMap);
        }
        // TODO create interceptor for admin verification
        return new ModelAndView("redirect:/home", modelMap);
    }

    private long calcAllItem() {
        return itemRepositoryProvider.baseItemRepositoryList.stream()
                .mapToLong(CrudRepository::count)
                .sum();
    }

    @GetMapping("/exit")
    public ModelAndView exit(ModelMap modelMap) {
        // TODO implement exit
        return new ModelAndView("redirect:/home", modelMap);
    }

}

