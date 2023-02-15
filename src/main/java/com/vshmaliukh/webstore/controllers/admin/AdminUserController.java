package com.vshmaliukh.webstore.controllers.admin;

import com.vshmaliukh.webstore.controllers.utils.TableContentImp;
import com.vshmaliukh.webstore.login.UserRole;
import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.repositories.UserRepository;
import com.vshmaliukh.webstore.services.OrderService;
import com.vshmaliukh.webstore.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.vshmaliukh.webstore.controllers.admin.AdminControllerUtils.generateTableContentForUserView;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin/user")
public class AdminUserController {

    final UserService userService;
    final OrderService orderService;

    @GetMapping("/**")
    public ModelAndView doGet(ModelMap modelMap) {
        return new ModelAndView("redirect:/admin/user/catalog", modelMap);
    }

    @GetMapping("/catalog")
    public ModelAndView doGetCatalog(@RequestParam(required = false) String keyword,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "id") String sortField,
                                     @RequestParam(defaultValue = "asc") String sortDirection,
                                     ModelMap modelMap) {
        UserRepository userRepository = userService.getUserRepository();
        TableContentImp<User> tableContentForUserView = generateTableContentForUserView(keyword, page, size, sortField, sortDirection, userRepository);
        List<User> userList = tableContentForUserView.readContentList();
        ModelMap contentModelMap = tableContentForUserView.readContentModelMap();

        modelMap.addAllAttributes(contentModelMap);
        modelMap.addAttribute("userList", userList);
        return new ModelAndView("/admin/user/catalog", modelMap);
    }

    @GetMapping("/create")
    public ModelAndView doGetCreate(ModelMap modelMap) {
        return new ModelAndView("/admin/user/create", modelMap);
    }

    @PostMapping("/create")
    public ModelAndView doPostCreate(@RequestParam("userName") String userName,
                                     @RequestParam("email") String email,
                                     @RequestParam("role") UserRole role,
                                     @RequestParam(value = "enabled", defaultValue = "false") boolean enabled,
                                     ModelMap modelMap) {
        User user = userService.createBaseUser(userName, email, role, enabled);
        ResponseEntity<Void> response = saveUser(user);
        if (!response.getStatusCode().isError()) {
            return new ModelAndView("redirect:/admin/user/catalog", modelMap);
        }
        return new ModelAndView("redirect:/admin/user/create", modelMap);
    }

    @PutMapping
    public ResponseEntity<Void> saveUser(@RequestBody User user) {
        if (user != null) {
            userService.save(user);
            if (userService.isUserSaved(user)) {
                log.info("successfully saved user to database: '{}'", user);
                return ResponseEntity.ok().build();
            }
        }
        log.warn("user entity '{}' not added to database", user);
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/view/{userId}")
    public ModelAndView doGetView(@PathVariable(name = "userId") Long userId,
                                  ModelMap modelMap) {
        Optional<User> optionalUser = userService.readUserById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            generateUserWithOrdersModel(modelMap, user);
            return new ModelAndView("/admin/user/view", modelMap);
        }
        return new ModelAndView("redirect:/admin/user/catalog", modelMap);
    }

    @GetMapping("/edit/{userId}")
    public ModelAndView doGetEdit(@PathVariable(name = "userId") Long userId,
                                  ModelMap modelMap) {
        // TODO implement another user 'edit' panel
        Optional<User> optionalUser = userService.readUserById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            generateUserWithOrdersModel(modelMap, user);
            return new ModelAndView("/admin/user/edit", modelMap);
        }
        return new ModelAndView("redirect:/admin/user/catalog", modelMap);
    }

    @PostMapping("/edit/{userId}")
    public ModelAndView doPostEdit(@PathVariable(name = "userId") Long userId,
                                   @RequestParam("userName") String userName,
                                   @RequestParam("email") String email,
                                   @RequestParam("role") UserRole role,
                                   @RequestParam(value = "enabled", defaultValue = "false") boolean enabled,
                                   ModelMap modelMap) {
        User user = userService.createBaseUser(userName, email, role, enabled);
        user.setId(userId);
        ResponseEntity<Void> response = saveUser(user);
        if (!response.getStatusCode().isError()) {
            return new ModelAndView("redirect:/admin/user/edit" + userId, modelMap);
        }
        return new ModelAndView("redirect:/admin/user/catalog", modelMap);
    }

    private void generateUserWithOrdersModel(ModelMap modelMap, User user) {
        List<Order> orderList = orderService.findUserOrderList(user);
        if (orderList == null) {
            orderList = Collections.emptyList();
        }
        modelMap.addAttribute( user);   
        modelMap.addAttribute("orderList", orderList);
    }

}

