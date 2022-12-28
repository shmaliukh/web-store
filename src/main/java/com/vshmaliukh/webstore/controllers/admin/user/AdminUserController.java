package com.vshmaliukh.webstore.controllers.admin.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin/user")
public class AdminUserController {

    @GetMapping("/**")
    public ModelAndView doGet(ModelMap modelMap) {
        return new ModelAndView("redirect:/admin/user/catalog", modelMap);
    }

    @GetMapping("/catalog")
    public ModelAndView doGetCatalog(ModelMap modelMap) {
        return new ModelAndView("/admin/user/catalog", modelMap);
    }

}

