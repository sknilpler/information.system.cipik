package com.information.system.cipik.controllers;

import com.information.system.cipik.models.*;
import com.information.system.cipik.repo.*;
import com.information.system.cipik.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    CentrRepository centrRepository;
    @Autowired
    KomplexRepository komplexRepository;
    @Autowired
    OtdelRepository otdelRepository;
    @Autowired
    RashodnikiRepository rashodnikiRepository;
    @Autowired
    SredstvoRepository sredstvoRepository;
    @Autowired
    NormaRepository normaRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private UserService userService;

    private Iterable<Sredstvo> listSredstv;
    private Sredstvo sredstvoAddToNorm;

//////////////пользователи created by Tashmetov Tahir////////////////////////

    @GetMapping("/admin/all-users")
    public String userList(Model model) {
        //Получаем пользователя, под которым выполнен вход (страница доступна только апдмину, соответсвенно пользователь будет только с ролью админа.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Получаем списко всех пользователей, для отображения на странице
        List<User> var_list = userService.allUsers();
        //Удаляем текущего пользователя из списка для отображения, чтобы он не мог удалить сам себя
        var_list.remove(userRepository.findByUsername(auth.getName()));
        Iterable<User> users = var_list;
        model.addAttribute("allUsers", users);
        return "admin/all-users";
    }

    //Формируем динамически страницу для каждого пользователя. Внутри страницы можно сделать операции над пользователем
    @GetMapping("/admin/all-users/user-details/{id}")
    public String userDetails(@PathVariable(value = "id") long id, Model model) {
        if (!userRepository.existsById(id)) {
            return "redirect:/admin/all-users";
        }
        Optional<User> user = userRepository.findById(id);
        ArrayList<User> res = new ArrayList<>();
        user.ifPresent(res::add);
        model.addAttribute("user", res);
        //Создаем объект var_AdminRole и добавляем его в атрибуты страницы.
        //чтобы потом определять пользователей у которого есть такая роль и не выводить кнопку "Сделать администратором" на странице user-details
        Role var_AdminRole = new Role(2L, "ROLE_ADMIN");
        model.addAttribute("var_AdminRole", var_AdminRole);
        return "admin/user-details";
    }

    @PostMapping("/admin/all-users/user-details/{id}/add_admin")
    public String add_admin(@PathVariable(value = "id") long id,Model model) {
        User user = userRepository.findById(id).orElseThrow();
        user.getRoles().add(new Role(2L, "ROLE_ADMIN"));
        userRepository.save(user);
        return "redirect:/admin/all-users";
    }

    @PostMapping("/admin/all-users/user-details/{id}/remove")
    public String delete_user(@PathVariable(value = "id") long id,Model model) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return "redirect:/admin/all-users";
    }

////////////////центра///////////////
    @GetMapping("/admin/centrs/add")
    public String centrAll(Model model) {
        Iterable<Centr> centrs = centrRepository.findAll();
        model.addAttribute("centrs", centrs);
        return "admin/admin-centr";
    }

    @PostMapping("/admin/centrs/add")
    public String centrAdd(@RequestParam String name, @RequestParam String shortName, Model model) {
        Centr cent = new Centr(name, shortName);
        centrRepository.save(cent);
        return "redirect:/admin/centrs/add";
    }

    @GetMapping("/admin/centrs/{id}/edit")
    public String centrEdit(@PathVariable(value = "id") long id, Model model) {
        if (!centrRepository.existsById(id)) {
            return "redirect:/admin/centrs/add";
        }
        Centr centr = centrRepository.findById(id).orElseThrow();
        model.addAttribute("centr", centr);
        return "admin/admin-centr-edit";
    }

    @PostMapping("/admin/centrs/{id}/edit")
    public String centrUpdate(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String shortName, Model model) {
        Centr centr = centrRepository.findById(id).orElseThrow();
        centr.setName(name);
        centr.setShortName(shortName);
        centrRepository.save(centr);
        return "redirect:/admin/centrs/add";
    }

    @PostMapping("/admin/centrs/{id}/remove")
    public String centrDelete(@PathVariable(value = "id") long id, Model model) {
        Centr centr = centrRepository.findById(id).orElseThrow();
        centrRepository.deleteById(id);
        return "redirect:/admin/centrs/add";
    }

    ////////////////////комплексы////////////////////////////////
    @GetMapping("/admin/komplexs/add")
    public String komplexAll(Model model) {
        Iterable<Centr> centrs = centrRepository.findAll();
        Iterable<Komplex> komplexs = komplexRepository.findAll();
        model.addAttribute("centrs", centrs);
        model.addAttribute("komplexs", komplexs);
        return "admin/admin-komplex";
    }

    @PostMapping("/admin/komplexs/add")
    public String komplexAdd(@RequestParam String name, @RequestParam String shortName, @RequestParam String adres, @RequestParam Long dropCentr, Model model) {
        Centr centr = centrRepository.findById(dropCentr).orElseThrow();
        Komplex komplex = new Komplex(name, adres, shortName, centr);
        komplexRepository.save(komplex);
        return "redirect:/admin/komplexs/add";
    }

    @GetMapping("/admin/komplexs/{id}/edit")
    public String komplexEdit(@PathVariable(value = "id") long id, Model model) {
        if (!komplexRepository.existsById(id)) {
            return "redirect:/admin/komplexs/add";
        }
        Iterable<Centr> centrs = centrRepository.findAll();
        Komplex komplex = komplexRepository.findById(id).orElseThrow();
        model.addAttribute("centrs", centrs);
        model.addAttribute("komplex", komplex);
        return "admin/admin-komplex-edit";
    }

    @PostMapping("/admin/komplexs/{id}/edit")
    public String komplexUpdate(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String shortName, @RequestParam String adres, @RequestParam Centr centr, Model model) {
        Komplex komplex = komplexRepository.findById(id).orElseThrow();
        komplex.setName(name);
        komplex.setShortName(shortName);
        komplex.setAdres(adres);
        komplex.setCentr(centr);
        komplexRepository.save(komplex);
        return "redirect:/admin/komplexs/add";
    }

    @PostMapping("/admin/komplexs/{id}/remove")
    public String komplexDelete(@PathVariable(value = "id") long id, Model model) {
        Komplex komplex = komplexRepository.findById(id).orElseThrow();
        komplexRepository.deleteById(id);
        return "redirect:/admin/komplexs/add";
    }
    ///////////////////отделы/////////////////////////////

    @GetMapping("/admin/otdels/add")
    public String otdelAll(Model model) {
        Iterable<Otdel> otdels = otdelRepository.findAll();
        Iterable<Komplex> komplexs = komplexRepository.findAll();
        model.addAttribute("otdels", otdels);
        model.addAttribute("komplexs", komplexs);
        return "admin/admin-otdel";
    }

    @PostMapping("/admin/otdels/add")
    public String otdelAdd(@RequestParam String name, @RequestParam Long dropKomplex, Model model) {
        Komplex komplex = komplexRepository.findById(dropKomplex).orElseThrow();
        Otdel otdel = new Otdel(name, komplex);
        otdelRepository.save(otdel);
        return "redirect:/admin/otdels/add";
    }

    @GetMapping("/admin/otdels/{id}/edit")
    public String otdelEdit(@PathVariable(value = "id") long id, Model model) {
        if (!otdelRepository.existsById(id)) {
            return "redirect:/admin/otdels/add";
        }
        Iterable<Komplex> komplexs = komplexRepository.findAll();
        Otdel otdel = otdelRepository.findById(id).orElseThrow();
        model.addAttribute("komplexs", komplexs);
        model.addAttribute("otdel", otdel);
        return "admin/admin-otdel-edit";
    }

    @PostMapping("/admin/otdels/{id}/edit")
    public String otdelUpdate(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam Komplex komplex, Model model) {
        Otdel otdel = otdelRepository.findById(id).orElseThrow();
        otdel.setName(name);
        otdel.setKomplex(komplex);
        otdelRepository.save(otdel);
        return "redirect:/admin/otdels/add";
    }

    @PostMapping("/admin/otdels/{id}/remove")
    public String otdelDelete(@PathVariable(value = "id") long id, Model model) {
        Otdel otdel = otdelRepository.findById(id).orElseThrow();
        otdelRepository.deleteById(id);
        return "redirect:/admin/otdels/add";
    }
    //////////////расходники///////////
    @GetMapping("/admin/rashs/add")
    public String rashAll(Model model) {
        Iterable<Rashodniki> rashodnikis = rashodnikiRepository.findAll();
        model.addAttribute("rashs", rashodnikis);
        return "admin/admin-rashodniki";
    }

    @PostMapping("/admin/rashs/add")
    public String rashAdd(@RequestParam String name, @RequestParam String ed_izm, Model model) {
        Rashodniki rash = new Rashodniki(name, ed_izm);
        rashodnikiRepository.save(rash);
        return "redirect:/admin/rashs/add";
    }

    @GetMapping("/admin/rashs/{id}/edit")
    public String rashEdit(@PathVariable(value = "id") long id, Model model) {
        if (!rashodnikiRepository.existsById(id)) {
            return "redirect:/admin/rashs/add";
        }
        Rashodniki rash = rashodnikiRepository.findById(id).orElseThrow();
        model.addAttribute("rash", rash);
        return "admin/admin-rashodniki-edit";
    }

    @PostMapping("/admin/rashs/{id}/edit")
    public String rashUpdate(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String ed_izm, Model model) {
        Rashodniki rash = rashodnikiRepository.findById(id).orElseThrow();
        rash.setName(name);
        rash.setEd_izm(ed_izm);
        rashodnikiRepository.save(rash);
        return "redirect:/admin/rashs/add";
    }

    @PostMapping("/admin/rashs/{id}/remove")
    public String rashDelete(@PathVariable(value = "id") long id, Model model) {
        Rashodniki rashodniki = rashodnikiRepository.findById(id).orElseThrow();
        rashodnikiRepository.deleteById(id);
        return "redirect:/admin/rashs/add";
    }

    //////////////////средства//////////////////////

    @GetMapping("/station/sredstvos")
    public String sredstvoAll(Model model) {
        Iterable<Sredstvo> sredstvos = sredstvoRepository.findAll();
        model.addAttribute("sredstvos", sredstvos);
        return "station/station-type-list";
    }

    @GetMapping("/station/sredstvos/add")
    public String sredstvoAdd(Model model) {
        return "station/station-type-add";
    }

    @PostMapping("/station/sredstvos/add")
    public String sredstvoAdding(@RequestParam String name, @RequestParam String indeks, Model model) {
        Sredstvo sredstvo = new Sredstvo(name, indeks);
        sredstvoRepository.save(sredstvo);
        return "redirect:/station/sredstvos";
    }

    @GetMapping("/station/sredstvos/{id}/edit")
    public String sredstvoEdit(@PathVariable(value = "id") long id, Model model) {
        if (!sredstvoRepository.existsById(id)) {
            return "redirect:/station/sredstvos";
        }
        Sredstvo sredstvo = sredstvoRepository.findById(id).orElseThrow();
        model.addAttribute("sredstvo", sredstvo);
        return "station/station-type-edit";
    }

    @PostMapping("/station/sredstvos/{id}/edit")
    public String sredstvoUpdate(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String indeks, Model model) {
        Sredstvo sredstvo = sredstvoRepository.findById(id).orElseThrow();
        sredstvo.setName(name);
        sredstvo.setIndeks(indeks);
        sredstvoRepository.save(sredstvo);
        return "redirect:/station/sredstvos";
    }

    @PostMapping("/station/sredstvos/{id}/remove")
    public String sredstvoDelete(@PathVariable(value = "id") long id, Model model) {
        Sredstvo sredstvo = sredstvoRepository.findById(id).orElseThrow();
        sredstvoRepository.deleteById(id);
        return "redirect:/station/sredstvos";
    }

    //////////////////нормы расхода//////////////////////

    @GetMapping("/admin/norms")
    public String normsAll(Model model, String keyword) {
        Iterable<Norma> normas = null;
        sredstvoAddToNorm = new Sredstvo("","");
        if (keyword != null) {
            listSredstv = sredstvoRepository.findAllByKeyword(keyword);
            model.addAttribute("sredstvos", listSredstv);
        } else {
            Iterable<Sredstvo> sredstvos = sredstvoRepository.findAll();
            model.addAttribute("sredstvos", sredstvos);
            listSredstv = sredstvos;
        }
        model.addAttribute("selected", sredstvoAddToNorm);
        model.addAttribute("norms", normas);
        return "admin/admin-norms";
    }

    @GetMapping("/admin/norms/getNormsForSredstvo/{id}")
    public String normsForSredstvo(@PathVariable(value = "id") long id, Model model) {
        Iterable<Norma> normas = normaRepository.findAllBySredstvoId(id);
        sredstvoAddToNorm = sredstvoRepository.findById(id).orElseThrow();
        model.addAttribute("norms", normas);
        model.addAttribute("sredstvos",listSredstv);
        model.addAttribute("selected", sredstvoAddToNorm);
        return "admin/admin-norms";
    }

    @GetMapping("/admin/norms/add")
    public String normsAdd(Model model) {
        if (!sredstvoAddToNorm.getName().equals("")) {
            model.addAttribute("rashodniks", rashodnikiRepository.findAll());
            model.addAttribute("sredstvo", sredstvoAddToNorm);
            return "admin/admin-norm-add";
        } else {
            return "redirect:/admin/norms";
        }
    }

    @PostMapping("/admin/norms/add")
    public String normsAdding(@RequestParam Long dropRash, @RequestParam double number, Model model) {
        if (!sredstvoAddToNorm.getName().equals("")) {
            Rashodniki rashodniki = rashodnikiRepository.findById(dropRash).orElseThrow();
            Norma norma = new Norma(sredstvoAddToNorm, rashodniki, number);
            normaRepository.save(norma);
        }
        return "redirect:/admin/norms";
    }

    @GetMapping("/admin/norms/{id}/edit")
    public String normsEdit(@PathVariable(value = "id") long id, Model model) {
        if (!normaRepository.existsById(id)) {
            return "redirect:/admin/norms";
        }
        Norma norma = normaRepository.findById(id).orElseThrow();
        model.addAttribute("rashodniks",rashodnikiRepository.findAll());
        model.addAttribute("sredstvo",sredstvoAddToNorm);
        model.addAttribute("norma", norma);
        return "admin/admin-norms-edit";
    }

    @PostMapping("/admin/norms/{id}/edit")
    public String normsUpdate(@PathVariable(value = "id") long id, @RequestParam Rashodniki dropRash, @RequestParam double number, Model model) {
        System.out.println(dropRash.getName());
        Norma norma = normaRepository.findById(id).orElseThrow();
        norma.setNumber(number);
        //norma.setRashodniki(rashodnikiRepository.findById(dropRash).orElseThrow());
        normaRepository.save(norma);
        return "redirect:/admin/norms";
    }

    @PostMapping("/admin/norms/{id}/remove")
    public String normsDelete(@PathVariable(value = "id") long id, Model model) {
        Norma norma = normaRepository.findById(id).orElseThrow();
        normaRepository.deleteById(id);
        return "redirect:/admin/norms";
    }
}
