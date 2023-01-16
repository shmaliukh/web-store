package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.services.CategoryService;
import com.vshmaliukh.webstore.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/category")
@AllArgsConstructor
public class AdminCategoryController {

    final ItemService itemService;
    final CategoryService categoryService;

    @GetMapping("/catalog")
    public ModelAndView doGetCatalog(ModelMap modelMap) {
        List<Category> categoryList = categoryService.readAll();
        
        modelMap.addAttribute("categoryList", categoryList);
        return new ModelAndView("admin/category/catalog", modelMap);
    }

    @GetMapping("create")
    public ModelAndView doGetCreate(ModelMap modelMap) {
        return new ModelAndView("admin/category/create", modelMap);
    }

    @PostMapping
    public ModelAndView doPostCreate(@RequestParam(name = "name") 

            ModelMap modelMap) {
        Category category;
        return new ModelAndView("redirect:admin/category/details/" + category.getId(), modelMap);
    }

}
