package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.services.CategoryService;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/admin/category")
@AllArgsConstructor
public class AdminCategoryController {

    final ItemService itemService;
    final CategoryService categoryService;

    @GetMapping("/catalog")
    public ModelAndView doGetCatalog(ModelMap modelMap){
        return new ModelAndView("admin/category/catalog", modelMap);
    }

}
