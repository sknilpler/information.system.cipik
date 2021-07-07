package com.information.system.cipik.controllers;


import com.information.system.cipik.models.*;
import com.information.system.cipik.repo.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {
    @Autowired
    public ItemsRepository itemsRepository;
    @Autowired
    public ArticleRepository articleRepository;
    @Autowired
    public ComingRepository comingRepository;
    @Autowired
    public KomplexRepository  komplexRepository;
    @Autowired
    public IssuanceItemsRepository issuanceItemsRepository;
    @Autowired
    public WriteOffActRepository writeOffActRepository;

    @GetMapping("/userPage/item-all")
    public String allItem(Model model) {
        List<Item> items = itemsRepository.findAllNotEmpty();
        model.addAttribute("allItems", items);
        return "user/mto/item/item-all";
    }

    ////////////////////статьи расхода//////////////////////////

    /**
     * Загрузка всех статей расхода
     */
    @GetMapping("/userPage/article-all")
    public String articleAll(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "user/mto/item/article/article-all";
    }

    /**
     * Добавление новой статьи
     *
     * @param name наименование
     * @param type тип
     */
    @PostMapping("/userPage/article-all")
    public String addArticle(@RequestParam String name, @RequestParam String type, Model model) {
        Article article = new Article(name, type);
        articleRepository.save(article);
        return "redirect:/userPage/article-all";
    }

    /**
     * Редактирование статьи
     *
     * @param id ID статьи
     */
    @GetMapping("/userPage/article/{id}/edit")
    public String editArticle(@PathVariable(value = "id") long id, Model model) {
        Article article = articleRepository.findById(id).orElseThrow();
        model.addAttribute("article", article);
        return "user/mto/item/article/article-edit";
    }

    /*
     * Сохранение статьи после редактирования
     *
     * @param id   ID статьи
     * @param name наименование
     * @param type тип
     */
    @PostMapping("/userPage/article/{id}/edit")
    public String updateArticle(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String type, Model model) {
        Article article = articleRepository.findById(id).orElseThrow();
        article.setName(name);
        article.setType(type);
        articleRepository.save(article);
        return "redirect:/userPage/article-all";
    }

    /**
     * Удаление статьи расхода
     *
     * @param id ID статьи
     */
    @PostMapping("/userPage/article/{id}/remove")
    public String deleteArticle(@PathVariable(value = "id") long id, Model model) {
        articleRepository.deleteById(id);
        return "redirect:/userPage/article-all";
    }

    ///////////////////////приход//////////////////////////

    /*
     * Открытие страницы добавления нового МЦ на склад
     */
    @GetMapping("/userPage/coming-add")
    public String getNewComing(Model model) {
        model.addAttribute("now", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        model.addAttribute("articles", articleRepository.findAll());
        return "user/mto/item/coming/coming-new";
    }

    /*
     * Сохранение нового МЦ на склад
     */
    @PostMapping("/userPage/coming-add-new")
    public String addItem(@RequestParam Article article, @RequestParam String name,
                            @RequestParam String number, @RequestParam String price, @RequestParam String unit,
                            @RequestParam String code, @RequestParam String date, @RequestParam String bill,
                            @RequestParam String nomenclature, Model model){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateReceive = new Date();
        try {
            dateReceive = format.parse(date);
        } catch (ParseException e) {
           // e.printStackTrace();
            System.out.println("Не удалось преобразовать дату полученную от клиента, будет установлена текущая дата");
        }
        Item itemOnStorage = itemsRepository.findByNameAndUnitAndCodeAndArticleId(name, unit, code, article.getId());
        if (itemOnStorage != null){
            double num = itemOnStorage.getNumber();
            itemOnStorage.setNumber(num+Double.parseDouble(number));
            itemOnStorage.setPrice(price);
            itemsRepository.save(itemOnStorage);
            comingRepository.save(new Coming(name,bill,number,price,unit,code,dateReceive,itemOnStorage));
        }else{
            Item item = new Item(bill,name,Double.parseDouble(number),unit,code,nomenclature,article,price);
            comingRepository.save(new Coming(name,bill,number,price,unit,code,dateReceive,itemsRepository.save(item)));
        }

        return "redirect:/userPage/item-all";
    }

    /*
     * Открытие страницы добавления прихода к МЦ
     */
    @GetMapping("/userPage/add-coming-for-item/{id}")
    public String addComing(@PathVariable("id") long id, Model model){
        Item item = itemsRepository.findById(id).orElseThrow();
        model.addAttribute("items",itemsRepository.findAll());
        model.addAttribute("item",item);
    return "user/mto/item/coming/new-coming-item";
    }

    /*
     * Сохранение нового прихода для МЦ
     */
    @PostMapping("/userPage/add-coming-for-item/{id}")
    public String saveComing(@PathVariable("id") long id,@RequestParam Article article, @RequestParam String name,
                             @RequestParam String number, @RequestParam String price, @RequestParam String unit,
                             @RequestParam String code, @RequestParam String date, @RequestParam String bill,
                             @RequestParam String nomenclature, Model model){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateReceive = new Date();
        try {
            dateReceive = format.parse(date);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("Не удалось преобразовать дату полученную от клиента, будет установлена текущая дата");
        }

        Item itemOnStorage = itemsRepository.findByNameAndUnitAndCodeAndArticleId(name, unit, code, article.getId());
        if (itemOnStorage != null){
            double num = itemOnStorage.getNumber();
            itemOnStorage.setNumber(num+Double.parseDouble(number));
            itemsRepository.save(itemOnStorage);
            comingRepository.save(new Coming(name,bill,number,price,unit,code,dateReceive,itemOnStorage));
        }else{
            System.out.println("Не найден МЦ на складе");
        }
        return "redirect:/userPage/item-all";
    }

    /*
     * Удаление МЦ из базы, также при этом удаляются все приходы, выдача и списание для удаляемой МЦ
     */
    @PostMapping("/userPage/item/{id}/remove")
    public String deleteItem(@PathVariable("id") Long id, Model model) {
        itemsRepository.deleteById(id);
        return "redirect:/userPage/item-all";
    }

///////////выдачи МЦ///////////////////

    /*
     * Открыть окно выдачи МЦ подразделениям
     */
    @PostMapping("/userPage/add-coming-for-item/{list}/issued")
    public String getIssuedItemPage(@PathVariable("list") List<Long> ids, Model model){
        List<Item> items = new ArrayList<>();
        for (Long id: ids) {
            Item item = itemsRepository.findById(id).orElseThrow();
            items.add(item);
        }
        model.addAttribute("komplexes",komplexRepository.findAll());
        model.addAttribute("items_issued",items);
        return "user/mto/item/blocks/issued-modal :: table-mc";
    }
    /*
     * Сохранение и печать выданных МЦ
     */
    @PostMapping("/userPage/add-coming-for-item/send/{id}")
    public void saveIssuedItem(@RequestBody Map<Long,String> objectList, HttpServletResponse response, @RequestParam String employeeAccepting,
                               @RequestParam String employeeGaveOut, @RequestParam String dateIssued, @RequestParam String invoiceNumber,
                               @PathVariable(value = "id") long id){
        Komplex komplex = komplexRepository.findById(id).orElseThrow();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateI = new Date();
        try {
            dateI = format.parse(dateIssued);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("Не удалось преобразовать дату полученную от клиента, будет установлена текущая дата");
        }
        for (Map.Entry<Long,String> entry : objectList.entrySet()) {
            Item item = itemsRepository.findById(entry.getKey()).orElseThrow();
            double num_new = Double.parseDouble(entry.getValue());
            item.setNumber(item.getNumber()-num_new);
            itemsRepository.save(item);
            issuanceItemsRepository.save(new IssuanceItems(num_new+"",dateI,invoiceNumber,employeeAccepting,employeeGaveOut,item,komplex));
        }
    }

    ////////////////////списание МЦ//////////////

    /*
     * Открытие страницы списания МЦ
     */
    @GetMapping("/userPage/add-write-off-for-item/{list}")
    public String getWriteOffItemPage(@PathVariable("list") List<Long> ids, Model model){
        List<Item> items = new ArrayList<>();
        for (Long id: ids) {
            Item item = itemsRepository.findById(id).orElseThrow();
            items.add(item);
        }
        model.addAttribute("komplexs",komplexRepository.findAll());
        model.addAttribute("items",items);
        return "user/mto/item/coming/write-off-act-new";
    }

    /*
     * Сохранения акта списания выбранных МЦ
     */
    @PostMapping("/userPage/add-write-off-for-item/send/{id}")
    public void saveWriteOffItem(@RequestBody List<Item> objectList, @RequestParam("file") MultipartFile file, HttpServletResponse response, @RequestParam String nameAct,
                                 @RequestParam String dateAct, @PathVariable(value = "id") long id) throws IOException {
        Komplex komplex = komplexRepository.findById(id).orElseThrow();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateWO = new Date();
        try {
            dateWO = format.parse(dateAct);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("Не удалось преобразовать дату полученную от клиента, будет установлена текущая дата");
        }
        for (Item i:objectList) {
            writeOffActRepository.save(new WriteOffAct(nameAct,dateWO,i.getNumber(),file.getName(), FilenameUtils.getExtension(file.getOriginalFilename()),file.getBytes(),i,komplex));
        }
    }


}