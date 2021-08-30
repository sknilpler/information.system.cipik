package com.information.system.cipik.controllers;


import com.information.system.cipik.models.*;
import com.information.system.cipik.repo.*;
import com.information.system.cipik.services.FileWriteOffActService;
import com.information.system.cipik.utils.ComingItemsExcelExporter;
import com.information.system.cipik.utils.IssuanceItemsExcelExporter;
import com.information.system.cipik.utils.WriteOffActExcelExporter;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    public FileWriteOffActService fileWriteOffActService;


    @GetMapping("/userPage/item-all")
    public String allItem(Model model) {
        List<Item> items = itemsRepository.findAllNotEmpty();
        model.addAttribute("allItems", items);
        return "user/mto/item/item-all";
    }

    /*
     * Поиск по таблице склада МЦ
     */
    @GetMapping("/userPage/item-all/search/{keyword}")
    public String searchItemsByKeyword(@PathVariable("keyword") String keyword, Model model){
        List<Item> items = new ArrayList<>();
        if (keyword.equals("0")) {
            items = itemsRepository.findAllNotEmpty();
        } else {
            items = itemsRepository.searchingItemsByKeyword(keyword);
        }
        model.addAttribute("allItems", items);
        return "user/mto/item/item-all :: item-table";
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
        Article article = articleRepository.findById(id).orElse(null);
        model.addAttribute("article", article);
        return "user/mto/item/article/article-edit";
    }

    /**
     * Сохранение статьи после редактирования
     *
     * @param id   ID статьи
     * @param name наименование
     * @param type тип
     */
    @PostMapping("/userPage/article/{id}/edit")
    public String updateArticle(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String type, Model model) {
        Article article = articleRepository.findById(id).orElse(null);
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
        Item itemOnStorage = itemsRepository.findByNameAndUnitAndNomenclatureAndArticleId(name, unit, code, article.getId());
        if (itemOnStorage != null){
            double num = itemOnStorage.getNumber();
            itemOnStorage.setNumber(num+Double.parseDouble(number));
            itemOnStorage.setPrice(price);
            itemsRepository.save(itemOnStorage);
            comingRepository.save(new Coming(name,bill,number,price,unit,nomenclature,dateReceive,itemOnStorage));
        }else{
            Item item = new Item(bill,name,Double.parseDouble(number),unit,code,nomenclature,article,price);
            comingRepository.save(new Coming(name,bill,number,price,unit,nomenclature,dateReceive,itemsRepository.save(item)));
        }

        return "redirect:/userPage/item-all";
    }

    /*
     * Открытие страницы добавления прихода к МЦ
     */
    @GetMapping("/userPage/add-coming-for-item/{id}")
    public String addComing(@PathVariable("id") long id, Model model){
        Item item = itemsRepository.findById(id).orElse(null);
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
                             @RequestParam String date, @RequestParam String bill,
                             @RequestParam String nomenclature, Model model){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateReceive = new Date();
        try {
            dateReceive = format.parse(date);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("Не удалось преобразовать дату полученную от клиента, будет установлена текущая дата");
        }

        Item itemOnStorage = itemsRepository.findByNameAndUnitAndNomenclatureAndArticleId(name, unit, nomenclature, article.getId());
        if (itemOnStorage != null){
            double num = itemOnStorage.getNumber();
            itemOnStorage.setNumber(num+Double.parseDouble(number));
            itemsRepository.save(itemOnStorage);
            comingRepository.save(new Coming(name,bill,number,price,unit,nomenclature,dateReceive,itemOnStorage));
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

    /*
     * Открытие страницы просмотра всех приходов
     */
    @GetMapping("/userPage/item/all-coming")
    public String getAllComings(Model model){
        model.addAttribute("comings",comingRepository.findAllByOrderByDateOfReceiveDesc());
        return "user/mto/item/coming/all-coming";
    }
    /*
     * Удаление прихода из базы
     */
    @PostMapping("/userPage/item/del-coming/{id}/remove")
    public String deleteComing(@PathVariable("id") long id,Model model){
        comingRepository.deleteById(id);
        model.addAttribute("comings",comingRepository.findAllByOrderByDateOfReceiveDesc());
        return "user/mto/item/coming/all-coming";
    }

    /*
     * Поиск по таблице прихода
     */
    @GetMapping("/userPage/item/coming/search/{keyword}")
    public String searchComingItemsByKeyword(@PathVariable("keyword") String keyword, Model model){
        Iterable<Coming> comings;
        if (keyword.equals("0")) {
            comings = comingRepository.findAllByOrderByDateOfReceiveDesc();
        } else {
            comings = comingRepository.searchComingsByKeyword(keyword);
        }
        model.addAttribute("comings", comings);
        return "user/mto/item/coming/all-coming :: coming-table";
    }

    /*
     * Поиск по таблице прихода с фильтрацией по дате
     */
    @GetMapping("/userPage/item/coming/search/{keyword}/filter/{date1}/{date2}")
    public String searchComingItemsByKeywordAndDate(@PathVariable("keyword") String keyword,
                                                    @PathVariable("date1") String date1,
                                                    @PathVariable("date2") String date2, Model model){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = new Date();
        Date d2 = new Date();
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Iterable<Coming> comings;
        if (keyword.equals("0")) {
            comings = comingRepository.findAllBetweenDates(d1,d2);
        } else {
            comings = comingRepository.searchComingsByKeywordAndDate(keyword,d1,d2);
        }
        model.addAttribute("comings", comings);
        return "user/mto/item/coming/all-coming :: coming-table";
    }

    /*
     * Фильтрация таблицы приходов по дате
     */
    @GetMapping("/userPage/item/coming/filter/{date1}/{date2}")
    public String filterComingItemsByDate(@PathVariable("date1") String date1,
                                          @PathVariable("date2") String date2, Model model){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = new Date();
        Date d2 = new Date();
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.addAttribute("comings", comingRepository.findAllBetweenDates(d1,d2));
        return "user/mto/item/coming/all-coming :: coming-table";
    }

    /*
     * Печать списка приходов
     */
    @GetMapping("/userPage/item/coming/print/{search}/{date1}/{date2}")
    public void printComingItems(HttpServletResponse response, @PathVariable(value = "search") String search,
                                 @PathVariable(value = "date1") String date1,
                                 @PathVariable(value = "date2") String date2) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=spisok_prihodov.xlsx";
        response.setHeader(headerKey, headerValue);

        Iterable<Coming> comings;
        if (!date1.equals("0") && !date2.equals("0")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = new Date();
            Date d2 = new Date();
            try {
                d1 = format.parse(date1);
                d2 = format.parse(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (search.equals("0")) {
                comings = comingRepository.findAllBetweenDates(d1,d2);
            } else {
                comings = comingRepository.searchComingsByKeywordAndDate(search,d1,d2);
            }
        } else {
            if (search.equals("0")) {
                comings = comingRepository.findAllByOrderByDateOfReceiveDesc();
            } else {
                comings = comingRepository.searchComingsByKeyword(search);
            }
        }

        ComingItemsExcelExporter excelExporter = new ComingItemsExcelExporter((List<Coming>) comings);
        excelExporter.export(response);
    }

    /*
     * Открытие страницы просмотра всех выдач МЦ
     */
    @GetMapping("/userPage/item/all-issued")
    public String getAllIssuedItems(Model model){
        model.addAttribute("issued",issuanceItemsRepository.findAllByOrderByDateIssuedDesc());
        model.addAttribute("komplexs", komplexRepository.findAll());
        return "user/mto/item/issuance-items/issuance-items-new";
    }

    /*
     * Удаление выдачи МЦ из базы
     */
    @PostMapping("/userPage/item/del-issued/{id}/remove")
    public String deleteIssuedItem(@PathVariable("id") long id, Model model){
        issuanceItemsRepository.deleteById(id);
        model.addAttribute("issued",issuanceItemsRepository.findAllByOrderByDateIssuedDesc());
        return "user/mto/item/issuance-items/issuance-items-new";
    }

    /*
     * Поиск по таблице выдачи МЦ
     */
    @GetMapping("/userPage/item/issuance-items/search/{keyword}")
    public String searchIssuanceItemsByKeyword(@PathVariable("keyword") String keyword, Model model){
        Iterable<IssuanceItems> items;
        if (keyword.equals("0")) {
            items = issuanceItemsRepository.findAllByOrderByDateIssuedDesc();
        } else {
            items = issuanceItemsRepository.searchByKeyword(keyword);
        }
        model.addAttribute("issued", items);
        return "user/mto/item/issuance-items/issuance-items-new :: table-issued-items";
    }


    /*
     * Поиск по таблице выдачи МЦ с фильтрацией по дате
     */
    @GetMapping("/userPage/item/issuance-items/search/{keyword}/filter/{date1}/{date2}")
    public String searchIssuanceItemsByKeywordAndDate(@PathVariable("keyword") String keyword,
                                                    @PathVariable("date1") String date1,
                                                    @PathVariable("date2") String date2, Model model){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = new Date();
        Date d2 = new Date();
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Iterable<IssuanceItems> items;
        if (keyword.equals("0")) {
            items = issuanceItemsRepository.findAllBetweenDates(d1,d2);
        } else {
            items = issuanceItemsRepository.searchByKeywordAndBetweenDates(keyword,d1,d2);
        }
        model.addAttribute("issued", items);
        return "user/mto/item/issuance-items/issuance-items-new :: table-issued-items";
    }

    /*
     * Фильтрация таблицы выдачи МЦ по дате
     */
    @GetMapping("/userPage/item/issuance-items/filter/{date1}/{date2}")
    public String filterIssuanceItemsByDate(@PathVariable("date1") String date1,
                                          @PathVariable("date2") String date2, Model model){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = new Date();
        Date d2 = new Date();
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.addAttribute("issued", issuanceItemsRepository.findAllBetweenDates(d1,d2));
        return "user/mto/item/issuance-items/issuance-items-new :: table-issued-items";
    }

    /*
     * Печать списка выданного МЦ
     */
    @GetMapping("/userPage/item/issuance-items/print/{search}/{date1}/{date2}")
    public void printIssuanceItems(HttpServletResponse response, @PathVariable(value = "search") String search,
                                 @PathVariable(value = "date1") String date1,
                                 @PathVariable(value = "date2") String date2) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=spisok_vidannogo_mc.xlsx";
        response.setHeader(headerKey, headerValue);

        Iterable<IssuanceItems> items;
        if (!date1.equals("0") && !date2.equals("0")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = new Date();
            Date d2 = new Date();
            try {
                d1 = format.parse(date1);
                d2 = format.parse(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (search.equals("0")) {
                items = issuanceItemsRepository.findAllBetweenDates(d1,d2);
            } else {
                items = issuanceItemsRepository.searchByKeywordAndBetweenDates(search,d1,d2);
            }
        } else {
            if (search.equals("0")) {
                items = issuanceItemsRepository.findAllByOrderByDateIssuedDesc();
            } else {
                items = issuanceItemsRepository.searchByKeyword(search);
            }
        }

        IssuanceItemsExcelExporter excelExporter = new IssuanceItemsExcelExporter((List<IssuanceItems>) items);
        excelExporter.export(response);
    }

    /*
     * Открыть страницу со списком всех актов списаний МЦ
     */
    @GetMapping("/userPage/item/all-write-off-acts")
    public String getAllWriteOffActs(Model model){
        model.addAttribute("writeOff",writeOffActRepository.findAllByOrderByDateActDesc());
        return "user/mto/item/write-off-act/write-off-act-new";
    }

    /*
     * Скачивание с сервера файла акта списания
     */
    @GetMapping("/userPage/item/write-off-act/get/{id}")
    public ResponseEntity<Object> serveFileOfAct(@PathVariable("id") long id){
        FileWriteOffAct file = fileWriteOffActService.getFile(id);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(file.getData()));
        HttpHeaders headers = new HttpHeaders();

        String nameFileDownload = file.getName();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", nameFileDownload));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok().headers(headers).contentLength(file.getData().length).contentType(
        MediaType.parseMediaType(file.getType())).body(resource);
    }

   /*
    * Удаление акта списания
    */
    @PostMapping("/userPage/item/write-off-act/{id}/remove")
    public String deleteWriteOffAct(@PathVariable("id") long id, Model model){
        WriteOffAct writeOffAct = writeOffActRepository.findById(id).orElse(null);
        fileWriteOffActService.deleteFile(writeOffAct.getFileWriteOffAct().getId());
        writeOffActRepository.delete(writeOffAct);
        model.addAttribute("writeOff",writeOffActRepository.findAllByOrderByDateActDesc());
        return "user/mto/item/write-off-act/write-off-act-new";
    }

    /*
     * Поиск по таблице актов списания
     */
    @GetMapping("/userPage/item/write-off-act/search/{keyword}")
    public String searchWriteOffActByKeyword(@PathVariable("keyword") String keyword, Model model){
        Iterable<WriteOffAct> woa;
        if (keyword.equals("0")) {
            woa = writeOffActRepository.findAllByOrderByDateActDesc();
        } else {
            woa = writeOffActRepository.searchByKeyword(keyword);
        }
        model.addAttribute("writeOff", woa);
        return "user/mto/item/write-off-act/write-off-act-new :: table-write-off-act";
    }


    /*
     * Поиск по таблице актов списания с фильтрацией по дате
     */
    @GetMapping("/userPage/item/write-off-act/search/{keyword}/filter/{date1}/{date2}")
    public String searchWriteOffActByKeywordAndDate(@PathVariable("keyword") String keyword,
                                                      @PathVariable("date1") String date1,
                                                      @PathVariable("date2") String date2, Model model){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = new Date();
        Date d2 = new Date();
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Iterable<WriteOffAct> woa;
        if (keyword.equals("0")) {
            woa = writeOffActRepository.findAllBetweenDates(d1,d2);
        } else {
            woa = writeOffActRepository.searchByKeywordAndBetweenDates(keyword,d1,d2);
        }
        model.addAttribute("writeOff", woa);
        return "user/mto/item/write-off-act/write-off-act-new :: table-write-off-act";
    }

    /*
     * Фильтрация таблицы актов списания по дате
     */
    @GetMapping("/userPage/item/write-off-act/filter/{date1}/{date2}")
    public String filterWriteOffActByDate(@PathVariable("date1") String date1,
                                            @PathVariable("date2") String date2, Model model){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = new Date();
        Date d2 = new Date();
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.addAttribute("writeOff", writeOffActRepository.findAllBetweenDates(d1,d2));
        return "user/mto/item/write-off-act/write-off-act-new :: table-write-off-act";
    }

    /*
     * Печать списка актов списания
     */
    @GetMapping("/userPage/item/write-off-act/print/{search}/{date1}/{date2}")
    public void printWriteOffActs(HttpServletResponse response, @PathVariable(value = "search") String search,
                                   @PathVariable(value = "date1") String date1,
                                   @PathVariable(value = "date2") String date2) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=spisok_aktov_spisaniya.xlsx";
        response.setHeader(headerKey, headerValue);

        Iterable<WriteOffAct> woa;
        if (!date1.equals("0") && !date2.equals("0")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = new Date();
            Date d2 = new Date();
            try {
                d1 = format.parse(date1);
                d2 = format.parse(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (search.equals("0")) {
                woa = writeOffActRepository.findAllBetweenDates(d1,d2);
            } else {
                woa = writeOffActRepository.searchByKeywordAndBetweenDates(search,d1,d2);
            }
        } else {
            if (search.equals("0")) {
                woa = writeOffActRepository.findAllByOrderByDateActDesc();
            } else {
                woa = writeOffActRepository.searchByKeyword(search);
            }
        }

        WriteOffActExcelExporter excelExporter = new WriteOffActExcelExporter((List<WriteOffAct>) woa);
        excelExporter.export(response);
    }

///////////выдачи МЦ///////////////////

    /*
     * Открыть окно выдачи МЦ подразделениям
     */
    @PostMapping("/userPage/add-coming-for-item/{list}/issued")
    public String getIssuedItemPage(@PathVariable("list") List<Long> ids, Model model){
        List<Item> items = new ArrayList<>();
        for (Long id: ids) {
            Item item = itemsRepository.findById(id).orElse(null);
            items.add(item);
        }
        model.addAttribute("komplexes",komplexRepository.findAll());
        model.addAttribute("items_issued",items);
        return "user/mto/item/blocks/issued-modal :: table-mc";
    }

    /*
     * Сохранение и печать выданных МЦ
     */
    @PostMapping("/userPage/add-coming-for-item/send/{id}/{employeeAccepting}/{employeeGaveOut}/{dateIssued}/{invoiceNumber}")
    public void saveIssuedItem(@RequestBody Map<String,List<String>> listOfObjects, HttpServletResponse response,
                               @PathVariable(value ="employeeAccepting") String employeeAccepting,
                               @PathVariable(value ="employeeGaveOut") String employeeGaveOut,
                               @PathVariable(value ="dateIssued") String dateIssued,
                               @PathVariable(value ="invoiceNumber") String invoiceNumber,
                               @PathVariable(value = "id") long id){
        Komplex komplex = komplexRepository.findById(id).orElse(null);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateI = new Date();
        try {
            dateI = format.parse(dateIssued);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("Не удалось преобразовать дату полученную от клиента, будет установлена текущая дата");
        }
        System.out.println(listOfObjects.size());
        List<Item> items = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : listOfObjects.entrySet()) {
            Item item = itemsRepository.findById(Long.parseLong(entry.getKey())).orElse(null);
            double num_new = Double.parseDouble(entry.getValue().get(0));
            item.setNumber(item.getNumber()-num_new);
            itemsRepository.save(item);
            issuanceItemsRepository.save(new IssuanceItems(num_new+"",dateI,invoiceNumber,employeeAccepting,employeeGaveOut,item,komplex));
            item.setNumber(num_new);
            items.add(item);
        }

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";

        String headerValue = "attachment; filename=nakladnaya_vidachi_mc_podrazdeleniyu.xls";
        response.setHeader(headerKey, headerValue);

        String excelFilePath = Paths.get("").toAbsolutePath().toString() + File.separator + "template" + File.separator + "шаблон накладная выдачи МЦ.xls";

        try {
            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            sheet.getRow(3).getCell(0).setCellValue("ТРЕБОВАНИЕ-НАКЛАДНАЯ №"+invoiceNumber);
            SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy");
            sheet.getRow(9).getCell(0).setCellValue(format2.format(dateI));
            sheet.getRow(9).getCell(4).setCellValue("ЦИП ИК      "+komplex.getShortName());

            int row = 17;
            Row exampleRow = sheet.getRow(17);
            int count = 1;
            for (Item it : items) {
                if (it.getNumber() != 0) {
                    sheet.getRow(row).setRowStyle(exampleRow.getRowStyle());
                    sheet.getRow(row).setHeight(exampleRow.getHeight());
                    if (row>17){
                    sheet.addMergedRegion(new CellRangeAddress(row,row,2,3));}
                    for (int i = 0; i < 12; i++) {
                        sheet.getRow(row).getCell(i).setCellStyle(exampleRow.getCell(i).getCellStyle());
                    }
                    sheet.getRow(row).getCell(0).setCellValue(it.getBill());
                    sheet.getRow(row).getCell(2).setCellValue(it.getName());
                    sheet.getRow(row).getCell(4).setCellValue(it.getNomenclature());
                    sheet.getRow(row).getCell(5).setCellValue(it.getCode());
                    sheet.getRow(row).getCell(6).setCellValue(it.getUnit());
                    sheet.getRow(row).getCell(7).setCellValue(it.getNumber());
                    sheet.getRow(row).getCell(8).setCellValue(it.getNumber());
                    row++;
                    count++;
                }
            }

//            Row exampleRow2 = sheet.getRow(2);
//            for (int i = 0; i < 12; i++) {
//                sheet.getRow(19+items.size()).getCell(i).setCellStyle();
//            }
//            sheet.getRow(19+items.size()).getCell(0).setCellValue("Отпустил ");
//            sheet.getRow(19+items.size()).getCell(3).setCellValue(employeeGaveOut);
//            sheet.getRow(19+items.size()).getCell(4).setCellValue("Получил ");
//            sheet.getRow(19+items.size()).getCell(11).setCellValue(employeeAccepting);

            inputStream.close();

            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (IOException | EncryptedDocumentException
                ex) {
            ex.printStackTrace();
        }
    }

    ////////////////////списание МЦ//////////////

    /*
     * Открытие страницы списания МЦ
     */
    @PostMapping("/userPage/add-write-off-for-item/{list}/send")
    public String getWriteOffItemPage(@PathVariable("list") List<Long> ids, Model model){
        List<Item> items = new ArrayList<>();
        for (Long id: ids) {
            Item item = itemsRepository.findById(id).orElse(null);
            items.add(item);
        }
        System.out.println(items.size());
        model.addAttribute("komplexs_wo",komplexRepository.findAll());
        model.addAttribute("items_wo",items);
        return "user/mto/item/blocks/write-off-modal :: table-wo";
    }

    /*
     * Сохранения акта списания выбранных МЦ
     */
    @PostMapping("/userPage/write-off-for-item/send/{id}/{dateWriteOff}")
    public String saveWriteOffItem(@RequestParam("file") MultipartFile file,
                                   @RequestParam("listOfObjects") String s,
                                   @RequestParam("nameAct") String nameAct,
                                   @PathVariable("dateWriteOff") String dateAct,
                                   @PathVariable(value = "id") long id) throws IOException {

        Map<Long, Double> map = new HashMap<>();
        s = s.substring(1, s.length() - 2);
        s = s.replace("\"", "");
        String[] ss = s.split(",");
        for (String el : ss) {
            String[] els = el.split(":");
            map.put(Long.parseLong(els[0]), Double.parseDouble(els[1]));
        }

        Komplex komplex = komplexRepository.findById(id).orElse(null);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateI = new Date();
        try {
            dateI = format.parse(dateAct);
        } catch (ParseException e) {
            // e.printStackTrace();
            System.out.println("Не удалось преобразовать дату полученную от клиента, будет установлена текущая дата");
        }
        FileWriteOffAct fileWriteOffAct = null;
        try{
           fileWriteOffAct = fileWriteOffActService.store(file);
            System.out.println("Файл успешно загружен!");
        }catch (Exception e){
            System.out.println("Не удалось загрузить файл");
        }

        for (Map.Entry<Long, Double> entry: map.entrySet()) {
            Item item = itemsRepository.findById(entry.getKey()).orElse(null);
            writeOffActRepository.save(new WriteOffAct(nameAct,dateI,entry.getValue(),item,komplex,fileWriteOffAct));
        }

        return "redirect:/userPage/item-all";
    }


}