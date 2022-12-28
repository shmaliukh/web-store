package com.vshmaliukh.webstore.controllers.admin.user;

import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.repositories.UserRepository;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils.addAttributesForSortingAndPaging;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin/user")
public class AdminUserController {

    final UserService userService;
    final UserRepository userRepository;

    @GetMapping("/**")
    public ModelAndView doGet(ModelMap modelMap) {
        return new ModelAndView("redirect:/admin/user/catalog", modelMap);
    }

    @GetMapping("/catalog")
    public ModelAndView doGetCatalog(@RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "6") int size,
                                     @RequestParam(defaultValue = "id,asc") String[] sort,
                                     ModelMap modelMap) {
        List<User> userList = userService.readAllUserList();

        String sortField = sort[0];
        String sortDirection = sort[1];
        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

        Page<User> pageWithUsers = getPageWithUsers(keyword, modelMap, pageable);
        addAttributesForSortingAndPaging(size, modelMap, sortField, sortDirection, pageWithUsers);

        modelMap.addAttribute("userList", userList);
        return new ModelAndView("/admin/user/catalog", modelMap);
    }

    private Page<User> getPageWithUsers(String keyword, ModelMap modelMap, Pageable pageable) {
        Page<User> pageWithUsers;
        if (keyword == null) {
            pageWithUsers = userRepository.findAll(pageable);
        } else {
            pageWithUsers = userRepository.findByUsernameIgnoreCase(keyword, pageable);
            modelMap.addAttribute("keyword", keyword);
        }
        List<User> content = pageWithUsers.getContent();
        return pageWithUsers;
    }

}

