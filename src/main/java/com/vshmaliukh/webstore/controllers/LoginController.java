package com.vshmaliukh.webstore.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.LOGIN_PAGE;
import static com.vshmaliukh.webstore.controllers.ConstantsForControllers.MAIN_PAGE;

@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping
    public String redirectToLogin(){
        return "redirect:/" + LOGIN_PAGE;
    }

    @GetMapping("/" + LOGIN_PAGE)
    public ModelAndView showLoginPage(){
        // todo create login page
        return new ModelAndView("login-page");
    }

    @PostMapping("/" + LOGIN_PAGE)
    public String login(){
        // todo login
        return "redirect:/"+MAIN_PAGE;
    }

}
