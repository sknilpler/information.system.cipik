package com.information.system.cipik.controllers;


import com.information.system.cipik.models.Item;
import com.information.system.cipik.repo.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ItemController {
    @Autowired
    public ItemsRepository itemsRepository;

    @GetMapping("/userPage/item-all")
    public String allItem(Model model) {
        Iterable<Item> items = itemsRepository.findAll();
        model.addAttribute("allItems", items);
        return "userPage/item/item-all";
    }

    @GetMapping("/userPage/item-add")
    public String addItem(Model model) {
        model.addAttribute("newItem", new Item());
        return "userPage/item/item-add";
    }

    @PostMapping("/userPage/item-add")
    public String addItem(@ModelAttribute("newItem") Item newItem, Model model) {
        Item itemFromDB = itemsRepository.findByName(newItem.getName());
        if(itemFromDB != null){
            model.addAttribute("itemNameError", "Данная позиция уже существует в базе");
            return "userPage/item/item-add";
        }
        itemsRepository.save(newItem);
        return "redirect:/userPage/item-all";
    }
}