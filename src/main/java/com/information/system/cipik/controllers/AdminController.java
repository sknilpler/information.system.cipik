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

@Controller
public class AdminController {

    @Autowired
    CentrRepository centrRepository;
    @Autowired
    KomplexRepository komplexRepository;
    @Autowired
    OtdelRepository otdelRepository;


    @GetMapping("/admin/centrs/add")
    public String centrAll(Model model){
        Iterable<Centr> centrs = centrRepository.findAll();
        model.addAttribute("centrs",centrs);
        return "admin-centr";
    }

    @PostMapping("/admin/centrs/add")
    public String centrAdd(@RequestParam String name, @RequestParam String shortName, Model model){
        Centr cent = new Centr(name, shortName);
        centrRepository.save(cent);
        return "redirect:/admin/centrs/add";
    }

    @GetMapping("/admin/centrs/{id}/edit")
    public String centrEdit(@PathVariable(value = "id") long id, Model model){
        System.out.println("Edit button success id: "+id);
        if (!centrRepository.existsById(id)){
            return "redirect:/admin/centrs/add";
        }
        Centr centr = centrRepository.findById(id).orElseThrow();
        model.addAttribute("centr",centr);
        return "admin-centr-edit";
    }

    @PostMapping("/admin/centrs/{id}/edit")
    public String centrUpdate(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String shortName, Model model){
        Centr centr = centrRepository.findById(id).orElseThrow();
        centr.setName(name);
        centr.setShortName(shortName);
        centrRepository.save(centr);
        return "redirect:/admin/centrs/add";
    }

    @PostMapping("/admin/centrs/{id}/remove")
    public String centrDelete(@PathVariable(value = "id") long id , Model model){
        Centr centr = centrRepository.findById(id).orElseThrow();
        centrRepository.deleteById(id);
        return "redirect:/admin/centrs/add";
    }

////////////////////////////////////////////////////
    @GetMapping("/admin/komplexs/add")
    public String komplexAll(Model model){
        Iterable<Centr> centrs = centrRepository.findAll();
        Iterable<Komplex> komplexs = komplexRepository.findAll();
        model.addAttribute("centrs",centrs);
        model.addAttribute("komplexs",komplexs);
        return "admin-komplex";
    }

    @PostMapping("/admin/komplexs/add")
    public String komplexAdd(@RequestParam String name, @RequestParam String shortName, @RequestParam String adres, @RequestParam Long dropCentr, Model model){
        Centr centr = centrRepository.findById(dropCentr).orElseThrow();
        Komplex komplex = new Komplex(name,adres,shortName,centr);
        komplexRepository.save(komplex);
        return "redirect:/admin/komplexs/add";
    }

}
