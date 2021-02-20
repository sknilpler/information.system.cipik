package com.information.system.cipik.controllers;

import com.information.system.cipik.repo.CentrRepository;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.repo.OtdelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AdminController {

    @Autowired
    CentrRepository centrRepository;
    KomplexRepository komplexRepository;
    OtdelRepository otdelRepository;

}
