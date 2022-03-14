package com.example.itemam.controller;


import com.example.itemam.security.CurrentUser;
import com.example.itemam.service.CategoryService;
import com.example.itemam.service.ItemService;
import com.example.itemam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final ItemService itemService;
    private final CategoryService categoryService;

    @Value("${itemAm.upload.path}")
    private String imagePath;


    @GetMapping("/")
    public String main(ModelMap map) {
        map.addAttribute("users", userService.findAll());
        map.addAttribute("items", itemService.findAll());
        map.addAttribute("categories", categoryService.findAll());
        return "main";
    }


    @GetMapping("/{id}")
    public String itemsByCategory(@PathVariable int id, ModelMap map) {
        map.addAttribute("items", itemService.findItemsByCategoryId(id));
        map.addAttribute("items", itemService.findAll());
        map.addAttribute("categories", categoryService.findAll());
        return "main";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }


    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        InputStream inputStream = Files.newInputStream(Paths.get(imagePath + picName));
        return IOUtils.readAllBytes(inputStream);
    }

}
