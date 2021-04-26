package com.information.system.cipik.controllers;

import com.information.system.cipik.services.CSVService;
import com.information.system.cipik.utils.csv.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CSVController {

    @Autowired
    CSVService fileService;

    @PostMapping("/admin/upload_employees")
    public String uploadEmployeeFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                fileService.saveEmployee(file);
                message = "Загрузка файла успешна: " + file.getOriginalFilename();
                attributes.addFlashAttribute("uplEmplMessage", message);
                return "redirect:/admin";
            } catch (Exception e) {
                message = "Не удалось загрузить файл: " + file.getOriginalFilename() + "!";
                attributes.addFlashAttribute("uplEmplMessage", message);
                return "redirect:/admin";
            }
        }
        message = "Пожалуйста загрузите файл формата csv!";
        attributes.addFlashAttribute("uplEmplMessage", message);
        return "redirect:/admin";
    }

}
