package com.information.system.cipik.controllers;

import com.information.system.cipik.repo.IPMStandardRepository;
import com.information.system.cipik.repo.IssuedSIZRepository;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.repo.SIZRepository;
import com.information.system.cipik.utils.SIZForPurchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String allPurchasingSIZ(Model model) {
        List<SIZForPurchase> sizesForPurchase = new ArrayList<>();
        List<Object[]> objectList = issuedSIZRepository.getAllSIZForPurchase();
        for (Object[] obj : objectList) {
            if ((Integer.parseInt(obj[4].toString())-Integer.parseInt(obj[5].toString()))!= 0) {
                sizesForPurchase.add(new SIZForPurchase(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], Integer.parseInt(obj[4].toString()), Integer.parseInt(obj[5].toString())));
            }
        }
        sizesForPurchase.forEach(sizForPurchase -> System.out.println(sizForPurchase.toString()));
        model.addAttribute("ipms", sizesForPurchase);
        return "user/mto/siz/purchasing-table.html";
    }
}
