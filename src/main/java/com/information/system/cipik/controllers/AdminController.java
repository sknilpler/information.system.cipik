package com.information.system.cipik.controllers;

import com.information.system.cipik.models.Centr;
import com.information.system.cipik.models.Komplex;
import com.information.system.cipik.repo.CentrRepository;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.repo.OtdelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    CentrRepository centrRepository;
    KomplexRepository komplexRepository;
    OtdelRepository otdelRepository;


    @GetMapping("/admin/centrs/add")
    public String centrAdd(Model model){
        Iterable<Centr> centrs = centrRepository.findAll();
        model.addAttribute("centrs",centrs);
        return "admin-centr";
    }

    @PostMapping("/admin/centrs/add")
    public String centrPostAdd(@RequestParam String name, @RequestParam String shortName, Model model){
        System.out.println(name + ", "+ shortName);
        Centr cent = new Centr(name, shortName);
        centrRepository.save(cent);
        return "redirect:/admin/centrs/add";
    }

    @GetMapping("/admin/centr/{id}/edit")
    public String centrEdit(@PathVariable(value = "id") long id, Model model){
        if (!centrRepository.existsById(id)){
            return "redirect:/admin/centr/add";
        }
        Optional<Centr> centr = centrRepository.findById(id);
        ArrayList<Centr> res = new ArrayList<>();
        centr.ifPresent(res::add);
        model.addAttribute("centr",res);
        return "admin-centr-edit";
    }

    @PostMapping("/admin/centr/{id}/edit")
    public String centrUpdate(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String shortName, Model model){
        Centr centr = centrRepository.findById(id).orElseThrow();
        centr.setName(name);
        centr.setShortName(shortName);
        centrRepository.save(centr);
        return "redirect:/admin/centr/add";
    }

    @PostMapping("/admin/centr/{id}/remove")
    public String centrDelete(@PathVariable(value = "id") long id , Model model){
        Centr centr = centrRepository.findById(id).orElseThrow();
        centrRepository.deleteById(id);
        return "redirect:/admin/centr/add";
    }


    @GetMapping("/admin/komlexs/add")
    public String komplexAdd(Model model){
        Iterable<Komplex> komplexs = komplexRepository.findAll();
        model.addAttribute("komplexs",komplexs);
        return "admin-komplex";
    }

    @PostMapping("/admin/komlexs/add")
    public String komplexPostAdd(@RequestParam String name, @RequestParam String shortName, @RequestParam String adres, @RequestParam Long dropCentr, Model model){
        System.out.println(name + ", "+ shortName + ", " + adres+", "+dropCentr);
//        Optional<Centr> centr = centrRepository.findById(dropCentr);
//        Komplex komplex = new Komplex(name,adres,shortName,centrRepository.);
//        komplexRepository.save(komplex);
        return "redirect:/admin/komlexs/add";
    }

}
