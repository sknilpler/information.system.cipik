package com.information.system.cipik.controllers;

import com.information.system.cipik.models.Komplex;
import com.information.system.cipik.repo.IPMStandardRepository;
import com.information.system.cipik.repo.IssuedSIZRepository;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.repo.SIZRepository;
import com.information.system.cipik.utils.SIZForPurchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PurchasingController {

    @Autowired
    SIZRepository sizRepository;
    @Autowired
    IPMStandardRepository ipmStandardRepository;
    @Autowired
    KomplexRepository komplexRepository;
    @Autowired
    IssuedSIZRepository issuedSIZRepository;

    /**
     * Первоначальная загрузка страницы закупки СИЗ
     * @param model
     * @return
     */
    @GetMapping("/userPage/purchasing-table")
    public String purchasingSIZNow(Model model) {
        List<SIZForPurchase> sizesForPurchase = new ArrayList<>();
        List<Object[]> objectList = issuedSIZRepository.getAllSIZForPurchaseByNow();
        for (Object[] obj : objectList) {
            if ((Integer.parseInt(obj[5].toString())-Integer.parseInt(obj[6].toString()))!= 0) {
                sizesForPurchase.add(new SIZForPurchase(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], (String) obj[4], Integer.parseInt(obj[5].toString()), Integer.parseInt(obj[6].toString())));
            }
        }
       sizesForPurchase.forEach(sizForPurchase -> System.out.println(sizForPurchase.toString()));
        Iterable<Komplex> komplexes = komplexRepository.findAll();
        model.addAttribute("komplexs", komplexes);
        model.addAttribute("ipms", sizesForPurchase);
        return "user/mto/siz/purchasing-table";
    }

    /**
     * Закупочная таблица СИЗ по типу отображения
     * @param value
     * @param model
     * @return
     */
    @GetMapping("/userPage/purchasing-table/filter/{value}")
    public String purchasingSIZFilter(@PathVariable(value = "value") String value, Model model) {
        List<SIZForPurchase> sizesForPurchase = new ArrayList<>();
        List<Object[]> objectList = null;
        if (value.equals("next-year")) {
            objectList=issuedSIZRepository.getAllSIZForPurchaseByDate((Year.now().getValue()+1)+"_01_01",(Year.now().getValue()+1)+"_12_31");
        } else if (value.equals("overall")){
            objectList=issuedSIZRepository.getAllSIZForPurchase();
        } else {
            objectList = issuedSIZRepository.getAllSIZForPurchaseByNow();
        }
        for (Object[] obj : objectList) {
            if ((Integer.parseInt(obj[5].toString())-Integer.parseInt(obj[6].toString()))!= 0) {
                sizesForPurchase.add(new SIZForPurchase(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], (String) obj[4], Integer.parseInt(obj[5].toString()), Integer.parseInt(obj[6].toString())));
            }
        }
        model.addAttribute("ipms", sizesForPurchase);
        return "user/mto/siz/purchasing-table :: table-purchasing";
    }

    /**
     * Закупочная таблица СИЗ по типу отображения и по отделам
     * @param value
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/purchasing-table/by-komplex/{value}/{komplex}")
    public String purchasingSIZFilterByKomplex(@PathVariable(value = "value") String value, @PathVariable(value = "komplex") Long id, Model model) {
        List<SIZForPurchase> sizesForPurchase = new ArrayList<>();

        List<Object[]> objectList = null;
        if (value.equals("next-year")) {
            objectList = issuedSIZRepository.getAllSIZForPurchaseByDateAndKomplex((Year.now().getValue()+1)+"_01_01",(Year.now().getValue()+1)+"_12_31",id);
        } else if (value.equals("overall")){
            objectList = issuedSIZRepository.getAllSIZForPurchaseByKomplex(id);
        } else {
            objectList = issuedSIZRepository.getAllSIZForPurchaseByNowAndKomplex(id);
        }
        for (Object[] obj : objectList) {
            if ((Integer.parseInt(obj[5].toString())-Integer.parseInt(obj[6].toString()))!= 0) {
                sizesForPurchase.add(new SIZForPurchase(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], (String) obj[4], Integer.parseInt(obj[5].toString()), Integer.parseInt(obj[6].toString())));
            }
        }
        model.addAttribute("ipms", sizesForPurchase);
        return "user/mto/siz/purchasing-table :: table-purchasing";
    }
}
