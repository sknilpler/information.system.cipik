package com.information.system.cipik.controllers;

import com.information.system.cipik.models.Centr;
import com.information.system.cipik.models.Komplex;
import com.information.system.cipik.models.Otdel;
import com.information.system.cipik.models.Rashodniki;
import com.information.system.cipik.repo.CentrRepository;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.repo.OtdelRepository;
import com.information.system.cipik.repo.RashodnikiRepository;
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
    @Autowired
    RashodnikiRepository rashodnikiRepository;

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
}
