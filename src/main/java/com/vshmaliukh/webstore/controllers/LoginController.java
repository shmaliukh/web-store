package com.vshmaliukh.webstore.controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping
    public String redirectToLogin(){
        return "redirect:/login";
    }

    @GetMapping("/login")
    public ModelAndView showLoginPage(){
        // todo create login page
        return new ModelAndView("login-page");
    }

    @PostMapping("/login")
    public String login(){
        // todo login
        return "redirect:/main";
    }

}
