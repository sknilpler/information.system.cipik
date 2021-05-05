package com.information.system.cipik.controllers;

import com.information.system.cipik.models.IndividualProtectionMeans;
import com.information.system.cipik.repo.IPMStandardRepository;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.repo.SIZRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PurchasingController {

    @Autowired
    SIZRepository sizRepository;
    @Autowired
    IPMStandardRepository ipmStandardRepository;
    @Autowired
    KomplexRepository komplexRepository;

    @GetMapping("/userPage/purchasing-table")
    public String allPurchasingSIZ(Model model) {

        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("ipms", individualProtectionMeans);
        return "user/mto/siz/purchasing-table.html";
    }
}
