package com.vshmaliukh.webstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/access-denied")
public class AccessDeniedController {

    @GetMapping
    public ModelAndView doGet(){
        return new ModelAndView("access-denied");
    }

}
