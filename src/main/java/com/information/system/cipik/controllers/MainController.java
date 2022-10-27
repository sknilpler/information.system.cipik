package com.information.system.cipik.controllers;

import com.information.system.cipik.models.HistoryAuthorization;
import com.information.system.cipik.models.Komplex;
import com.information.system.cipik.repo.HistoryAuthorizationRepository;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.services.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Slf4j
@Controller
public class MainController {
    @Autowired
    KomplexRepository komplexRepository;

    @Autowired
    HistoryAuthorizationRepository historyAuthorizationRepository;
    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        Iterable<Komplex> komplexes = komplexRepository.findAll();
        model.addAttribute("komplexes", komplexes);
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("/success-login")
    public String successLogin(Authentication authentication){

        HistoryAuthorization newAuthorization = new HistoryAuthorization();
        newAuthorization.setUsername(authentication.getName());
        newAuthorization.setAuthorizationTime(new Date());
        log.warn(authentication.getName() + " logged in");
        historyAuthorizationRepository.save(newAuthorization);
        return "success-login";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Инфомация");
        return "about";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        String[] s = adminService.getDBSettings();
        String[] s2 = adminService.getPaths();
        model.addAttribute("user_name", s[0]);
        model.addAttribute("pass", s[1]);
        model.addAttribute("db_name", s[2]);
        model.addAttribute("mysqlPath", s2[0]);
        model.addAttribute("backupPath", s2[1]);
        model.addAttribute("title", "Администрирование основных данных");
        return "admin/admin";
    }

    @GetMapping("user/mto")
    public String mto(Model model) {
        model.addAttribute("title", "Главная страница МТО");
        return "user/mto/main-mto-page";
    }

    @GetMapping("user/ge")
    public String ge(Model model) {
        model.addAttribute("title", "Главная страница ГЭ");
        return "user/group-explotation/main-ge-page";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "login");
        return "login";
    }

}
