package com.information.system.cipik.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Инфомация");
        return "about";
    }

    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("title", "Администрирование основных данных");
        return "admin/admin";
    }

    @GetMapping("user/mto")
    public String mto(Model model){
        model.addAttribute("title", "Главная страница МТО");
        return "user/mto/main-mto-page";
    }

    @GetMapping("user/ge")
    public String ge(Model model){
        model.addAttribute("title", "Главная страница ГЭ");
        return "user/group-explotation/main-ge-page";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "login");
        return "login";
    }

}
