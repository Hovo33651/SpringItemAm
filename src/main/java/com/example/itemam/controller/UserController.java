package com.example.itemam.controller;

import com.example.itemam.entity.User;
import com.example.itemam.security.CurrentUser;
import com.example.itemam.service.CategoryService;
import com.example.itemam.service.ItemService;
import com.example.itemam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ItemService itemService;
    private final CategoryService categoryService;




    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userService.saveUser(user);
        return "loginPage";
    }


    @GetMapping("/home")
    public String homePage(ModelMap map, @AuthenticationPrincipal CurrentUser currentUser) {
        map.addAttribute("items", itemService.findAll());
        map.addAttribute("categories", categoryService.findAll());
        map.addAttribute("currentUser", currentUser);
        return "home";
    }

    @GetMapping("/home/{id}")
    public String homePage(ModelMap map, @PathVariable int id,
                           @AuthenticationPrincipal CurrentUser currentUser){
        map.addAttribute("users", userService.findAll());
        map.addAttribute("items", itemService.findItemsByCategoryId(id));
        map.addAttribute("categories", categoryService.findAll());
        map.addAttribute("currentUser", currentUser);
        return "home";
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }


    @GetMapping("/successLogin")
    public String login(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser != null) {
            return "redirect:/home";
        }else{
            return "redirect:/loginPage";
        }
    }

}
