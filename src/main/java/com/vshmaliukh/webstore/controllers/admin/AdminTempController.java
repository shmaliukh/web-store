package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.services.TempService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/test-data")
public class AdminTempController {

    final TempService tempService;

    @GetMapping
    public ModelAndView doGetAddData(ModelMap modelMap) {
        tempService.addDataToDatabase();
        return new ModelAndView("redirect:/admin/category/create", modelMap);
    }

}
