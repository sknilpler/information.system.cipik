package com.information.system.cipik.controllers;


import com.information.system.cipik.models.*;
import com.information.system.cipik.repo.*;
import com.information.system.cipik.services.FileWriteOffActService;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
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
    @Autowired
    public FileWriteOffActService fileWriteOffActService;

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
        Article article = articleRepository.findById(id).orElse(null);
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
    @PostMapping("/userPage/write-off-for-item/send/{id}/{dateWriteOff}/{nameAct}")
    public String saveWriteOffItem(@RequestParam MultipartFile file,
                                 @RequestParam Map<String,List<String>> listOfObjects,
                                 @PathVariable("nameAct") String nameAct,
                                 @PathVariable("dateWriteOff") String dateAct,
                                 @PathVariable(value = "id") long id) throws IOException {

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

        for (Map.Entry<String, List<String>> entry : listOfObjects.entrySet()) {
            Item item = itemsRepository.findById(Long.parseLong(entry.getKey())).orElse(null);
            double num_write_off = Double.parseDouble(entry.getValue().get(0));
            writeOffActRepository.save(new WriteOffAct(nameAct,dateI,num_write_off,item,komplex,fileWriteOffAct));
        }

        return "redirect:/userPage/item-all";
    }


}