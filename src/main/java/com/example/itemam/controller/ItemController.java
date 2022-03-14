package com.example.itemam.controller;

import com.example.itemam.dto.CreateItemRequest;
import com.example.itemam.entity.Item;
import com.example.itemam.exception.ItemNotFoundException;
import com.example.itemam.security.CurrentUser;
import com.example.itemam.service.CategoryService;
import com.example.itemam.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;


    @GetMapping("/addItem")
    public String addItem(ModelMap map){
        map.addAttribute("categories",categoryService.findAll());
        return "saveItem";
    }


    @PostMapping("/addItem")
    public String addItem(@ModelAttribute CreateItemRequest createItemRequest,
                          @RequestParam("pictures") MultipartFile[] uploadedFiles, @AuthenticationPrincipal CurrentUser currentUser) throws IOException {
        itemService.saveItemFromItemRequest(createItemRequest,uploadedFiles, currentUser);
        return "redirect:/home";
    }


    @GetMapping("/singleItem/{id}")
    public String singleItem(@PathVariable int id,@AuthenticationPrincipal CurrentUser currentUser, ModelMap map){
        try{
        map.addAttribute(itemService.findItemById(id));
        map.addAttribute("currentUser", currentUser);
        map.addAttribute("categories",categoryService.findAll());
        }catch (ItemNotFoundException e){
            StringBuilder msg = new StringBuilder();
            msg.append("Item is not available at this moment");
            map.addAttribute("msg",msg);
        }
        return "singleItem";
    }

    @GetMapping("/myItems/{id}")
    public String currentUserItem(@PathVariable int id, ModelMap map, @AuthenticationPrincipal CurrentUser currentUser){
        map.addAttribute("items", itemService.findItemsByUserId(id));
        map.addAttribute("categories", categoryService.findAll());
        map.addAttribute("currentUser", currentUser);
        return "home";
    }

    @GetMapping("/deleteItem/{id}")
    public String deleteItem(@PathVariable int id){
        itemService.deleteItemById(id);
        return "redirect:/home";
    }

}
