package com.information.system.cipik.controllers;


import com.information.system.cipik.models.Item;
import com.information.system.cipik.repo.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ItemController {
    @Autowired
    public ItemsRepository itemsRepository;

    @GetMapping("/userPage/item-all")
    public String allItem(Model model) {
        Iterable<Item> items = itemsRepository.findAll();
        model.addAttribute("allItems", items);
        return "user/mto/item/item-all";
    }

    @GetMapping("/userPage/item-add")
    public String addItem(Model model) {
        model.addAttribute("newItem", new Item());
        return "user/mto/item/item-add";
    }

    @PostMapping("/userPage/item-add")
    public String addItem(@ModelAttribute("newItem") Item newItem, Model model) {
        Item itemFromDB = itemsRepository.findByName(newItem.getName());
        if(itemFromDB != null){
            model.addAttribute("itemNameError", "Данная позиция уже существует в базе");
            return "user/mto/item/item-add";
        }
        itemsRepository.save(newItem);
        return "redirect:/userPage/item-all";
    }

    @PostMapping("/userPage/item/{id}/remove")
    public String delete_item(@PathVariable(value = "id") long id, Model model) {
        Item item = itemsRepository.findById(id).orElseThrow();
        itemsRepository.delete(item);
        return "redirect:/userPage/item-all";
    }

    @GetMapping("/userPage/item/{id}/edit")
    public String itemEdit(@PathVariable(value = "id") long id, Model model) {
        if (!itemsRepository.existsById(id)) {
            return "redirect:/userPage/item-all";
        }
        Item item = itemsRepository.findById(id).orElseThrow();
        model.addAttribute("item", item);
        return "user/mto/item/item-edit";
    }

    @PostMapping("/userPage/item/{id}/edit")
    public String itemUpdate(@PathVariable(value = "id") long id, @ModelAttribute("item") Item newItem, Model model) {
        newItem.setId(id);
        System.out.println(newItem.getId());
        itemsRepository.save(newItem);
        return "redirect:/userPage/item-all";
    }
}