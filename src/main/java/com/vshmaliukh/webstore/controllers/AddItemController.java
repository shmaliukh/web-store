package com.vshmaliukh.webstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.*;

@Controller
@RequestMapping("/" + ADD_ITEM_PAGE)
public class AddItemController {

    public ModelAndView doGet(ModelMap modelMap){
        return new ModelAndView(ADD_ITEM_PAGE, modelMap);
    }

}
