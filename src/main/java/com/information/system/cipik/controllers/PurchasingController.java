package com.information.system.cipik.controllers;

import com.information.system.cipik.models.Komplex;
import com.information.system.cipik.repo.IssuedSIZRepository;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.utils.PurchasingSIZExcelExporter;
import com.information.system.cipik.utils.SIZForPurchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PurchasingController {

    @Autowired
    KomplexRepository komplexRepository;
    @Autowired
    IssuedSIZRepository issuedSIZRepository;


    /**
     * Первоначальная загрузка страницы закупки СИЗ
     *
     * @param model модель аттрибутов страницы
     * @return веб-страница
     */
    @GetMapping("/userPage/purchasing-table")
    public String purchasingSIZNow(Model model) {
        List<SIZForPurchase> sizesForPurchase = new ArrayList<>();
        List<Object[]> objectList = issuedSIZRepository.getAllSIZForPurchaseByNow();
        for (Object[] obj : objectList) {
            if ((Integer.parseInt(obj[5].toString()) - Integer.parseInt(obj[6].toString())) != 0) {
                sizesForPurchase.add(new SIZForPurchase(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], (String) obj[4], Integer.parseInt(obj[5].toString()), Integer.parseInt(obj[6].toString())));
            }
        }
        Iterable<Komplex> komplexes = komplexRepository.findAll();
        model.addAttribute("komplexs", komplexes);
        model.addAttribute("ipms", sizesForPurchase);
        return "user/mto/siz/purchasing-table";
    }

    /**
     * Закупочная таблица СИЗ по типу отображения
     *
     * @param value значение фильтра
     * @param model модель аттрибутов страницы
     * @return фрагмент таблицы
     */
    @GetMapping("/userPage/purchasing-table/filter/{value}")
    public String purchasingSIZFilter(@PathVariable(value = "value") String value, Model model) {
        List<SIZForPurchase> sizesForPurchase = new ArrayList<>();
        List<Object[]> objectList = null;
        if (value.equals("next-year")) {
            objectList = issuedSIZRepository.getAllSIZForPurchaseByDate((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31");
        } else if (value.equals("overall")) {
            objectList = issuedSIZRepository.getAllSIZForPurchase();
        } else {
            objectList = issuedSIZRepository.getAllSIZForPurchaseByNow();
        }
        for (Object[] obj : objectList) {
            if ((Integer.parseInt(obj[5].toString()) - Integer.parseInt(obj[6].toString())) != 0) {
                sizesForPurchase.add(new SIZForPurchase(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], (String) obj[4], Integer.parseInt(obj[5].toString()), Integer.parseInt(obj[6].toString())));
            }
        }
        model.addAttribute("ipms", sizesForPurchase);
        return "user/mto/siz/purchasing-table :: table-purchasing";
    }

    /**
     * Закупочная таблица СИЗ по типу отображения и по отделам
     *
     * @param value значение фильтра
     * @param id    ID подразделения
     * @param model модель аттрибутов страницы
     * @return фрагмент таблицы
     */
    @GetMapping("/userPage/purchasing-table/by-komplex/{value}/{komplex}")
    public String purchasingSIZFilterByKomplex(@PathVariable(value = "value") String value, @PathVariable(value = "komplex") Long id, Model model) {
        List<SIZForPurchase> sizesForPurchase = new ArrayList<>();

        List<Object[]> objectList = null;
        if (value.equals("next-year")) {
            objectList = issuedSIZRepository.getAllSIZForPurchaseByDateAndKomplex((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", id);
        } else if (value.equals("overall")) {
            objectList = issuedSIZRepository.getAllSIZForPurchaseByKomplex(id);
        } else {
            objectList = issuedSIZRepository.getAllSIZForPurchaseByNowAndKomplex(id);
        }
        for (Object[] obj : objectList) {
            if ((Integer.parseInt(obj[5].toString()) - Integer.parseInt(obj[6].toString())) != 0) {
                sizesForPurchase.add(new SIZForPurchase(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], (String) obj[4], Integer.parseInt(obj[5].toString()), Integer.parseInt(obj[6].toString())));
            }
        }
        model.addAttribute("ipms", sizesForPurchase);
        return "user/mto/siz/purchasing-table :: table-purchasing";
    }

    /**
     * Печать закупочной таблицы
     *
     * @param response Http заголовок страницы
     * @param value    значение фильтра
     * @param id       ID подразделения
     * @throws IOException в случае неудачной печати
     */
    @GetMapping("/userPage/purchasing-table/print-table/{value}/{komplex}")
    public void printPurchasingTable(HttpServletResponse response, @PathVariable(value = "value") String value, @PathVariable(value = "komplex") Long id) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("dd_MM_yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=zakupochnaya_tablica_SIZ_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<SIZForPurchase> sizesForPurchase = new ArrayList<>();

        List<Object[]> objectList = null;

        if (id != 0) {
            if (value.equals("next-year")) {
                objectList = issuedSIZRepository.getAllSIZForPurchaseByDateAndKomplex((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", id);
            } else if (value.equals("overall")) {
                objectList = issuedSIZRepository.getAllSIZForPurchaseByKomplex(id);
            } else {
                objectList = issuedSIZRepository.getAllSIZForPurchaseByNowAndKomplex(id);
            }
        } else {
            if (value.equals("next-year")) {
                objectList = issuedSIZRepository.getAllSIZForPurchaseByDate((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31");
            } else if (value.equals("overall")) {
                objectList = issuedSIZRepository.getAllSIZForPurchase();
            } else {
                objectList = issuedSIZRepository.getAllSIZForPurchaseByNow();
            }
        }
        for (Object[] obj : objectList) {
            if ((Integer.parseInt(obj[5].toString()) - Integer.parseInt(obj[6].toString())) != 0) {
                sizesForPurchase.add(new SIZForPurchase(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], (String) obj[4], Integer.parseInt(obj[5].toString()), Integer.parseInt(obj[6].toString())));
            }
        }

        PurchasingSIZExcelExporter excelExporter = new PurchasingSIZExcelExporter(sizesForPurchase);
        excelExporter.export(response);
    }
}
