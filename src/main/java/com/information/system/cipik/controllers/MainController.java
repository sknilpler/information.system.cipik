package com.information.system.cipik.controllers;

import com.information.system.cipik.models.Komplex;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    KomplexRepository komplexRepository;
    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public String home(Model model) {
        Iterable<Komplex> komplexes = komplexRepository.findAll();
        model.addAttribute("komplexes",komplexes);
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
        String[] s = adminService.getDBSettings();
        model.addAttribute("user_name",s[0]);
        model.addAttribute("pass",s[1]);
        model.addAttribute("db_name",s[2]);
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
