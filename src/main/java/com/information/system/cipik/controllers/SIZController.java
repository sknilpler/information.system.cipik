package com.information.system.cipik.controllers;

import com.ibm.icu.text.Transliterator;
import com.information.system.cipik.models.*;
import com.information.system.cipik.repo.*;
import com.information.system.cipik.utils.*;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

@Controller
public class SIZController {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SIZRepository sizRepository;
    @Autowired
    IPMStandardRepository ipmStandardRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    KomplexRepository komplexRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    IssuedSIZRepository issuedSIZRepository;
    @Autowired
    SizeSizRepository sizeSizRepository;

    /**
     * Список должностей для норм выдачи
     */
    private Iterable<Post> listPosts;
    /**
     * Выбранная должность для норм выдачи
     */
    private Post postAddToNorm;
    /**
     * Сотрудник которому выдается СИЗ
     */
    private Employee employeeForIssuedSIZ;
    /**
     * Строка фильтрации для выданного СИЗ
     */
    private String filerIssuedSizAll;
    /**
     * Условие проверки сортировки по окончанию даты носки СИЗ
     */
    private boolean sortedByEndIssuedDate;
    /**
     * Тип сортировки таблицы укомплектованности сотрудников
     */
    private TypeOfSortingEmployeeTable typeOfSortingEmployeeTable;
    /**
     * Список ошибок при выдаче СИЗ сотруднику
     */
    private final List<String> listErrors = new ArrayList<>();


    private final List<IssuedSIZ> SIZForPrint = new ArrayList<>();

    /**
     * Первоначальное открытие страницы с типами СИЗ
     *
     * @param model модель аттрибутов страницы
     * @return веб страница со списком типов СИЗ
     */
    @GetMapping("/userPage/siz-types")
    public String allSIZ(Model model) {
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("ipms", individualProtectionMeans);
        return "user/mto/siz/types-of-siz";
    }

    /**
     * Добавление нового типа СИЗ
     *
     * @param nameSIZ            наименование СИЗ
     * @param ed_izm             единицы измерения
     * @param typeIPM            тип СИЗ
    // * @param nomenclatureNumber номенклатурный номер
     * @param model              модель аттрибутов страницы
     * @return перенаправление на /userPage/siz-types
     */
    @PostMapping("/userPage/siz-types")
    public String addSIZ(@RequestParam String nameSIZ, @RequestParam String ed_izm, @RequestParam String typeIPM, Model model) {
        IndividualProtectionMeans individualProtectionMeans = new IndividualProtectionMeans(nameSIZ, ed_izm, typeIPM);
        sizRepository.save(individualProtectionMeans);
        List<String> sizes = new ArrayList<>();
        if (typeIPM.equals("Одежда")) {
            List<String> heights = new ArrayList<>();
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "clothing-size.dat");
            File file2 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "height.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1));
                 BufferedReader reader2 = new BufferedReader(new FileReader(file2))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
                String height = reader2.readLine();
                while (height != null) {
                    heights.add(height);
                    height = reader2.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                for (String h : heights) {
                    sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s, h));
                }
            }
        }
        if (typeIPM.equals("Обувь")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "shoe-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }
        if (typeIPM.equals("Головной убор")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "head-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }
        if (typeIPM.equals("Перчатки")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "hand-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }
        if (typeIPM.equals("Рукавицы")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "hand-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }
        if (typeIPM.equals("Респиратор")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "respirator-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }
        if (typeIPM.equals("Противогаз")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "gas-mask-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }
        return "redirect:/userPage/siz-types";
    }

    /**
     * Открытие страницы для редактирования типа СИЗ
     *
     * @param id    ID выбранного для редактирования типа СИЗ
     * @param model модель аттрибутов страницы
     * @return страница для редактирования СИЗ
     */
    @GetMapping("/userPage/siz-types/{id}/edit")
    public String editSIZ(@PathVariable(value = "id") long id, Model model) {
        if (!sizRepository.existsById(id)) {
            return "redirect:/userPage/siz-types";
        }
        IndividualProtectionMeans individualProtectionMeans = sizRepository.findById(id).orElse(null);
        model.addAttribute("siz", individualProtectionMeans);
        return "user/mto/siz/types-of-siz-edit";
    }

    /**
     * Сохранение отредактированного типа СИЗ
     *
     * @param id                 ID типа СИЗ
     * @param nameSIZ            наименование СИЗ
     * @param ed_izm             единицы измерения
     * @param typeIPM            тип СИЗ
    // * @param nomenclatureNumber номенклатурный номер
     * @param model              модель аттрибутов страницы
     * @return перенаправление на /userPage/siz-types
     */
    @PostMapping("/userPage/siz-types/{id}/edit")
    public String updateSIZ(@PathVariable(value = "id") long id, @RequestParam String nameSIZ, @RequestParam String ed_izm, @RequestParam String typeIPM, Model model) {
        IndividualProtectionMeans individualProtectionMeans = sizRepository.findById(id).orElse(null);
        individualProtectionMeans.setNameSIZ(nameSIZ);
        individualProtectionMeans.setEd_izm(ed_izm);
        individualProtectionMeans.setTypeIPM(typeIPM);
        sizRepository.save(individualProtectionMeans);

        List<SizeSiz> sizeSizs = sizeSizRepository.findAllByIndividualProtectionMeansId(individualProtectionMeans.getId());

        sizeSizs.forEach(sizeSiz -> sizeSizRepository.deleteById(sizeSiz.getId()));

        List<String> sizes = new ArrayList<>();
        if (typeIPM.equals("Одежда")) {
            List<String> heights = new ArrayList<>();
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "clothing-size.dat");
            File file2 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "height.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1));
                 BufferedReader reader2 = new BufferedReader(new FileReader(file2))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
                String height = reader2.readLine();
                while (height != null) {
                    heights.add(height);
                    height = reader2.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                for (String h : heights) {
                    sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s, h));
                }
            }
        }
        if (typeIPM.equals("Обувь")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "shoe-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }
        if (typeIPM.equals("Головной убор")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "head-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }
        if (typeIPM.equals("Перчатки")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "hand-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }
        if (typeIPM.equals("Рукавицы")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "hand-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }
        if (typeIPM.equals("Респиратор")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "respirator-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }
        if (typeIPM.equals("Противогаз")) {
            File file1 = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "settings" + File.separator + "gas-mask-size.dat");
            try (BufferedReader reader1 = new BufferedReader(new FileReader(file1))) {
                String size = reader1.readLine();
                while (size != null) {
                    sizes.add(size);
                    size = reader1.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (String s : sizes) {
                sizeSizRepository.save(new SizeSiz(individualProtectionMeans, s));
            }
        }

        return "redirect:/userPage/siz-types";
    }

    /**
     * Удаление выбранного типа СИЗ
     *
     * @param id    ID типа СИЗ
     * @param model модель аттрибутов страницы
     * @return перенаправление на /userPage/siz-types
     */
    @PostMapping("/userPage/siz-types/{id}/remove")
    public String deleteSIZ(@PathVariable(value = "id") long id, Model model) {
        IndividualProtectionMeans individualProtectionMeans = sizRepository.findById(id).orElse(null);
        sizRepository.deleteById(id);
        return "redirect:/userPage/siz-types";
    }

    /**
     * Открытие страницы размеров выбранного типа СИЗ
     *
     * @param id    ID выбранного типа СИЗ
     * @param model модель аттрибутов страницы
     * @return страницу с размерами
     */
    @GetMapping("/userPage/siz-types/sizes/{id}")
    public String openSizeSizPage(@PathVariable(value = "id") long id, Model model) {
        IndividualProtectionMeans individualProtectionMeans = sizRepository.findById(id).orElse(null);
        model.addAttribute("siz", individualProtectionMeans);
        model.addAttribute("sizes", sizeSizRepository.findAllByIndividualProtectionMeansId(id));
        return "user/mto/siz/sizes";
    }

    /**
     * Сохранение нового размера для выбранного типа СИЗ
     *
     * @param id     ID выбранного типа СИЗ
     * @param height рост
     * @param size   размер
     * @param model  модель аттрибутов страницы
     * @return страницу с размерами
     */
    @PostMapping("/userPage/siz-types/sizes/{id}")
    public String addSizeAndHeightToSIZ(@PathVariable(value = "id") long id, @RequestParam(required = false) String height, @RequestParam String size, Model model) {
        IndividualProtectionMeans individualProtectionMeans = sizRepository.findById(id).orElse(null);
        SizeSiz sizeSiz;
        if ((height == null)||(height.equals(""))) {
            sizeSiz = new SizeSiz(individualProtectionMeans, size);
        } else {
            sizeSiz = new SizeSiz(individualProtectionMeans, size, height);
        }
        sizeSizRepository.save(sizeSiz);
        model.addAttribute("siz", individualProtectionMeans);
        model.addAttribute("sizes", sizeSizRepository.findAllByIndividualProtectionMeansId(id));
        return "user/mto/siz/sizes";
    }


    /**
     * Удаление размера из выбранного типа СИЗ
     *
     * @param id    ID размера
     * @param model модель аттрибутов страницы
     * @return страницу с размерами
     */
    @PostMapping("/userPage/sizes/{id}/remove")
    public String deleteSizeFromSiz(@PathVariable(value = "id") long id, Model model) {
        SizeSiz sizeSiz = sizeSizRepository.findById(id).orElse(null);
        IndividualProtectionMeans individualProtectionMeans = sizeSiz.getIndividualProtectionMeans();
        sizeSizRepository.deleteById(id);
        model.addAttribute("siz", individualProtectionMeans);
        model.addAttribute("sizes", sizeSizRepository.findAllByIndividualProtectionMeansId(individualProtectionMeans.getId()));
        return "redirect:/userPage/siz-types/sizes/" + individualProtectionMeans.getId();
    }

    //////////////////нормы СИЗ//////////////////////

    /**
     * Первоначальное открытие страницы с нормами выдачи СИЗ
     *
     * @param model   модель аттрибутов страницы
     * @param keyword строка поиска
     * @return страница со списком норм
     */
    @GetMapping("/userPage/siz/norms")
    public String normsAll(Model model, String keyword) {
        //Iterable<IPMStandard> normas = null;
        postAddToNorm = new Post("");
        if (keyword != null) {
            listPosts = postRepository.findAllByKeyword(keyword);
            model.addAttribute("posts", listPosts);
        } else {
            Iterable<Post> posts = postRepository.findAll();
            model.addAttribute("posts", posts);
            listPosts = posts;
        }
        model.addAttribute("selected", postAddToNorm);
        model.addAttribute("norms", null);

//        List<SizeSiz> list = new ArrayList<>();
//        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
//        for (IndividualProtectionMeans ipm: individualProtectionMeans) {
//            if (ipm.getTypeIPM().equals("Одежда")){
//                list.add(new SizeSiz(ipm,"40-42","1-2"));
//                list.add(new SizeSiz(ipm,"40-42","3-4"));
//                list.add(new SizeSiz(ipm,"40-42","5-6"));
//                list.add(new SizeSiz(ipm,"40-42","7-8"));
//                list.add(new SizeSiz(ipm,"44-46","1-2"));
//                list.add(new SizeSiz(ipm,"44-46","3-4"));
//                list.add(new SizeSiz(ipm,"44-46","5-6"));
//                list.add(new SizeSiz(ipm,"44-46","7-8"));
//                list.add(new SizeSiz(ipm,"48-50","1-2"));
//                list.add(new SizeSiz(ipm,"48-50","3-4"));
//                list.add(new SizeSiz(ipm,"48-50","5-6"));
//                list.add(new SizeSiz(ipm,"48-50","7-8"));
//                list.add(new SizeSiz(ipm,"52-54","1-2"));
//                list.add(new SizeSiz(ipm,"52-54","3-4"));
//                list.add(new SizeSiz(ipm,"52-54","5-6"));
//                list.add(new SizeSiz(ipm,"52-54","7-8"));
//                list.add(new SizeSiz(ipm,"56-58","1-2"));
//                list.add(new SizeSiz(ipm,"56-58","3-4"));
//                list.add(new SizeSiz(ipm,"56-58","5-6"));
//                list.add(new SizeSiz(ipm,"56-58","7-8"));
//                list.add(new SizeSiz(ipm,"60-62","1-2"));
//                list.add(new SizeSiz(ipm,"60-62","3-4"));
//                list.add(new SizeSiz(ipm,"60-62","5-6"));
//                list.add(new SizeSiz(ipm,"60-62","7-8"));
//            }
//            if (ipm.getTypeIPM().equals("Обувь")){
//                for (int i = 0; i < 15; i++) {
//                    list.add(new SizeSiz(ipm,(35+i)+"",""));
//                }
//            }
//            if (ipm.getTypeIPM().equals("Головной убор")){
//                for (int i = 0; i < 17; i++) {
//                    list.add(new SizeSiz(ipm,(50+i)+"",""));
//                }
//            }
//            if (ipm.getTypeIPM().equals("Перчатки")){
//                list.add(new SizeSiz(ipm,"7.0",""));
//                list.add(new SizeSiz(ipm,"7.5",""));
//                list.add(new SizeSiz(ipm,"8.0",""));
//                list.add(new SizeSiz(ipm,"8.5",""));
//                list.add(new SizeSiz(ipm,"9.0",""));
//                list.add(new SizeSiz(ipm,"9.5",""));
//                list.add(new SizeSiz(ipm,"10.0",""));
//                list.add(new SizeSiz(ipm,"10.5",""));
//                list.add(new SizeSiz(ipm,"11.0",""));
//                list.add(new SizeSiz(ipm,"11.5",""));
//                list.add(new SizeSiz(ipm,"12.0",""));
//            }
//            if (ipm.getTypeIPM().equals("Рукавицы")){
//                list.add(new SizeSiz(ipm,"6.0",""));
//                list.add(new SizeSiz(ipm,"6.5",""));
//                list.add(new SizeSiz(ipm,"7.0",""));
//                list.add(new SizeSiz(ipm,"7.5",""));
//                list.add(new SizeSiz(ipm,"8.0",""));
//                list.add(new SizeSiz(ipm,"8.5",""));
//                list.add(new SizeSiz(ipm,"9.0",""));
//                list.add(new SizeSiz(ipm,"9.5",""));
//            }
//            if (ipm.getTypeIPM().equals("Респиратор")){
//                list.add(new SizeSiz(ipm,"1",""));
//                list.add(new SizeSiz(ipm,"2",""));
//                list.add(new SizeSiz(ipm,"3",""));
//            }
//            if (ipm.getTypeIPM().equals("Противогаз")){
//                list.add(new SizeSiz(ipm,"0",""));
//                list.add(new SizeSiz(ipm,"1",""));
//                list.add(new SizeSiz(ipm,"2",""));
//                list.add(new SizeSiz(ipm,"3",""));
//                list.add(new SizeSiz(ipm,"4",""));
//            }
//        }
//        System.out.println(list.size());
//        for (SizeSiz s: list) {
//            sizeSizRepository.save(s);
//        }


        return "user/mto/siz/norms/siz-norms";
    }

    /**
     * Загрузка на страницу норм выдачи для выбранной должности
     *
     * @param id    ID выбранной должности
     * @param model модель аттрибутов страницы
     * @return страница норм выдачи с отображенными данными по выбранной должности
     */
    @GetMapping("/userPage/siz/norms/getNormsForPost/{id}")
    public String normsForPost(@PathVariable(value = "id") long id, Model model) {
        Iterable<IPMStandard> normas = ipmStandardRepository.findAllByPostId(id);
        postAddToNorm = postRepository.findById(id).orElse(null);
        model.addAttribute("norms", normas);
        model.addAttribute("posts", listPosts);
        model.addAttribute("selected", postAddToNorm);
        return "user/mto/siz/norms/siz-norms";
    }

    /**
     * Открытие страницы создания новой нормы выдачи для выбранной должности
     *
     * @param model модель аттрибутов страницы
     * @return веб страница
     */
    @GetMapping("/userPage/siz/norms/add")
    public String normsAdd(Model model) {
        if (!postAddToNorm.getPostName().equals("")) {
            model.addAttribute("sizs", sizRepository.findAll());
            model.addAttribute("post", postAddToNorm);
            return "user/mto/siz/norms/siz-norms-add";
        } else {
            return "redirect:/userPage/siz/norms";
        }
    }

    /**
     * Сохранение новой нормы выдачи для выбранного СИЗ
     *
     * @param dropSIZ             ID типа СИЗ
     * @param issuanceRate        нормы выдачи (кол-во)
     * @param serviceLife         срок носки
     * @param regulatoryDocuments регламентирующий документ
     * @param model               модель аттрибутов страницы
     * @return перенаправление на /userPage/siz/norms
     */
    @PostMapping("/userPage/siz/norms/add")
    public String normsAdding(@RequestParam Long dropSIZ, @RequestParam int issuanceRate,
                              @RequestParam int serviceLife, @RequestParam String regulatoryDocuments, Model model) {
        if (!postAddToNorm.getPostName().equals("")) {
            IndividualProtectionMeans siz = sizRepository.findById(dropSIZ).orElse(null);
            IPMStandard norma = new IPMStandard(postAddToNorm, siz, issuanceRate, serviceLife, regulatoryDocuments);
            ipmStandardRepository.save(norma);
        }
        return "redirect:/userPage/siz/norms";
    }

    /**
     * Открытие страницы редактирования нормы выдачи СИЗ
     *
     * @param id    ID нормы
     * @param model модель аттрибутов страницы
     * @return веб станица
     */
    @GetMapping("/userPage/siz/norms/{id}/edit")
    public String normsEdit(@PathVariable(value = "id") long id, Model model) {
        if (!ipmStandardRepository.existsById(id)) {
            return "redirect:/userPage/siz/norms";
        }
        IPMStandard norma = ipmStandardRepository.findById(id).orElse(null);
        model.addAttribute("sizs", sizRepository.findAll());
        model.addAttribute("post", postAddToNorm);
        model.addAttribute("norma", norma);
        return "user/mto/siz/norms/siz-norms-edit";
    }

    /**
     * Сохранение изменений после редактирования норм выдачи
     *
     * @param id                  ID нормы
     * @param dropSIZ             ID типа СИЗ
     * @param issuanceRate        норма выдачи (кол-во)
     * @param serviceLife         срок носки
     * @param regulatoryDocuments регламентирующий документ
     * @param model               модель аттрибутов страницы
     * @return перенаправление на /userPage/siz/norms
     */
    @PostMapping("/userPage/siz/norms/{id}/edit")
    public String normsUpdate(@PathVariable(value = "id") long id, @RequestParam Long dropSIZ, @RequestParam int issuanceRate,
                              @RequestParam int serviceLife, @RequestParam String regulatoryDocuments, Model model) {
        IndividualProtectionMeans siz = sizRepository.findById(dropSIZ).orElse(null);
        IPMStandard norma = ipmStandardRepository.findById(id).orElse(null);
        norma.setIndividualProtectionMeans(siz);
        norma.setIssuanceRate(issuanceRate);
        norma.setServiceLife(serviceLife);
        norma.setRegulatoryDocuments(regulatoryDocuments);
        //norma.setRashodniki(rashodnikiRepository.findById(dropRash).orElse(null));
        ipmStandardRepository.save(norma);
        return "redirect:/userPage/siz/norms";
    }

    /**
     * Удаление нормы выдачи СИЗ
     *
     * @param id    ID нормы
     * @param model модель аттрибутов страницы
     * @return перенаправление на /userPage/siz/norms
     */
    @PostMapping("/userPage/siz/norms/{id}/remove")
    public String normsDelete(@PathVariable(value = "id") long id, Model model) {
        IPMStandard norma = ipmStandardRepository.findById(id).orElse(null);
        ipmStandardRepository.deleteById(id);
        return "redirect:/userPage/siz/norms";
    }

    ////////////////СИЗ на складе///////////////////

    /**
     * Первоначальное открытие страницы склада СИЗ
     *
     * @param model модель аттрибутов страницы
     * @return веб страница
     */
    @GetMapping("/userPage/not-issued-siz")
    public String notIssuedSIZAll(Model model, Authentication authentication) {
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        List<SIZForStock> sizesForStock = new ArrayList<>();
        List<Object[]> objectList;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")) {
            objectList = issuedSIZRepository.findGroupbySizeAndHeight();
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            objectList = issuedSIZRepository.findGroupbySizeAndHeightByKomplex(komplex.getId());
            model.addAttribute("komplex", komplex);
        }
        for (Object[] obj : objectList) {
            sizesForStock.add(new SIZForStock(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], (String) obj[4], Integer.parseInt(obj[5].toString())));
        }
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("typeSIZS", individualProtectionMeans);
        model.addAttribute("notIssuedSIZ", sizesForStock);
        return "user/mto/siz/siz-from-stock";
    }

    /**
     * Отправка на страницу списка размеров для конкретного СИЗ
     *
     * @param id    ID выбранного типа СИЗ
     * @param model модель атрибутов страницы
     * @return фрагмент со списком размеров
     */
    @GetMapping("/userPage/not-issued-siz/get-sizes/{id}")
    public String getSizesOfSIZ(@PathVariable(value = "id") long id, Model model) {
        List<SizeSiz> sizeSizs = sizeSizRepository.findAllByIndividualProtectionMeansId(id);
        List<String> temp = new ArrayList<>();
        for (SizeSiz s : sizeSizs) {
            temp.add(s.getSize());
        }
        List<String> sizes = temp.stream().distinct().collect(Collectors.toList());
        model.addAttribute("sizes", sizes);
        return "user/mto/siz/siz-from-stock :: list-sizes";
    }

    /**
     * Добавление нового СИЗ
     *
     * @param typeSIZ ID типа СИЗ
     * @param size    размер
     * @param height  рост
     * @param model   модель аттрибутов страницы
     * @return веб страница
     */
    @PostMapping("/userPage/not-issued-siz")
    public String notIssuedSIZAdd(@RequestParam Long typeSIZ, @RequestParam String size, @RequestParam String height, @RequestParam String nomenclatureNumber, @RequestParam int number, Model model) {
        IndividualProtectionMeans ipm = sizRepository.findById(typeSIZ).orElse(null);
        IssuedSIZ issuedSIZ;
        for (int i = 0; i < number; i++) {
            if (height.equals("non")) {
                issuedSIZ = new IssuedSIZ(ipm, size, nomenclatureNumber);
            } else {
                issuedSIZ = new IssuedSIZ(ipm, size, height, nomenclatureNumber);
            }
            issuedSIZRepository.save(issuedSIZ);
        }
        return "redirect:/userPage/not-issued-siz";
    }

    /**
     * Редактирование данных о СИЗ
     *
     * @param id    id SIZ
     * @param model model from page
     * @return page
     */
    @GetMapping("/userPage/not-issued-siz/{id}/{size}/{height}/{number}/{nomenclatureNumber}/edit")
    public String notIssuedSIZEdit(@PathVariable(value = "id") long id, @PathVariable(value = "nomenclatureNumber") String nomenclatureNumber, @PathVariable(value = "height") String height, @PathVariable(value = "size") String size, @PathVariable(value = "number") int number, Model model) {
        IssuedSIZ siz;
        if ((height == null) || (height.equals("")) || (height.equals("non"))) {
            siz = issuedSIZRepository.findByStock(size, id, nomenclatureNumber, "На складе").get(0);
        } else {
            siz = issuedSIZRepository.findByStock(size, height, id, nomenclatureNumber, "На складе").get(0);
        }
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("typeSIZS", individualProtectionMeans);
        model.addAttribute("siz", siz);
        model.addAttribute("number", number);
        return "user/mto/siz/siz-from-stock-edit";
    }

    /**
     * Сохранение отредактированных данных о СИЗ
     *
     * @param id     ID СИЗ на складе
     * @param siz    тип СИЗ
     * @param size   размер
     * @param height рост
     * @param model  модель аттрибутов страницы
     * @return веб страница
     */
    @PostMapping("/userPage/not-issued-siz/{id}/edit")
    public String notIssuedSIZUpdate(@PathVariable(value = "id") long id, @RequestParam IndividualProtectionMeans siz, @RequestParam String size, @RequestParam String height, @RequestParam String nomenclatureNumber, @RequestParam int number, Model model) {
        List<IssuedSIZ> list;
        IssuedSIZ old_siz = issuedSIZRepository.findById(id).orElse(null);
        if (old_siz != null) {
            if ((old_siz.getHeight() == null) || old_siz.getHeight().equals("") || old_siz.getHeight().equals("non") || old_siz.getHeight().equals("null")) {
                list = issuedSIZRepository.findByStock(old_siz.getSize(), siz.getId(), old_siz.getNomenclatureNumber(), old_siz.getStatus());
                for (IssuedSIZ s : list) {
                   // System.out.println("Updating (deleting): " + s.toString());
                    issuedSIZRepository.delete(s);
                }
                for (int i = 0; i < number; i++) {
                    issuedSIZRepository.save(new IssuedSIZ(siz, size, nomenclatureNumber));
                }
            } else {
                list = issuedSIZRepository.findByStock(old_siz.getSize(), old_siz.getHeight(), siz.getId(), old_siz.getNomenclatureNumber(), old_siz.getStatus());
                for (IssuedSIZ s : list) {
                  //  System.out.println("Updating (deleting): " + s.toString());
                    issuedSIZRepository.delete(s);
                }
                for (int i = 0; i < number; i++) {
                    issuedSIZRepository.save(new IssuedSIZ(siz, size, height, nomenclatureNumber));
                }
            }
        }
        return "redirect:/userPage/not-issued-siz";
    }

    /**
     * Удаление выбранного СИЗ
     *
     * @param list  список СИЗ на складе для удаления
     * @param model модель аттрибутов страницы
     * @return веб страница
     */
    @PostMapping("/userPage/not-issued-siz/{list}/remove")
    public String notIssuedSIZDelete(@PathVariable(value = "list") List<String> list, Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        List<SIZForStock> sizesForStock = new ArrayList<>();
        List<Object[]> objectList;
        if (role.getName().equals("ROLE_USER")) {    //если пользователь СуперЮзер то просто удаляем записи из базы
            for (String str : list) {
                String[] arrData = str.split("_");
                long id = Long.parseLong(arrData[0]);
                String size = arrData[1];
                String height = arrData[2];
                String nomenclatureNumber = arrData[3];
                List<IssuedSIZ> sizs;
                if ((height == null) || (height.equals("")) || (height.equals("null"))) {
                    sizs = issuedSIZRepository.findByStock(size, id, nomenclatureNumber, "На складе");
                } else {
                    sizs = issuedSIZRepository.findByStock(size, height, id, nomenclatureNumber, "На складе");
                    System.out.println("is not null");
                }
                for (IssuedSIZ s : sizs) {
                    System.out.println("Deleting: "+s.toString());
                    issuedSIZRepository.deleteById(s.getId());
                }
            }
            objectList = issuedSIZRepository.findGroupbySizeAndHeight();
        } else {      //иначе возвращаем СИЗ на главный склад центра
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            for (String str : list) {
                String[] arrData = str.split("_");
                long id = Long.parseLong(arrData[0]);
                String size = arrData[1];
                String height = arrData[2];
                String nomenclatureNumber = arrData[3];
                List<IssuedSIZ> sizs;
                if ((height == null) || (height.equals("")) || (height.equals("null"))) {
                    sizs = issuedSIZRepository.findByStockAndKomplex(size, id, "На складе", nomenclatureNumber, komplex.getId());
                } else {
                    sizs = issuedSIZRepository.findByStockAndKomplex(size, height, id, "На складе", nomenclatureNumber, komplex.getId());
                }
                for (IssuedSIZ s : sizs) {
                    s.setKomplex(null);
                    System.out.println("Return to storage: "+s.toString());
                    issuedSIZRepository.save(s);
                }
            }
            objectList = issuedSIZRepository.findGroupbySizeAndHeightByKomplex(komplex.getId());
        }
        for (Object[] obj : objectList) {
            sizesForStock.add(new SIZForStock(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], (String) obj[4], Integer.parseInt(obj[5].toString())));
        }
        model.addAttribute("notIssuedSIZ", sizesForStock);
        return "user/mto/siz/siz-from-stock :: table-sock-siz";
    }

    /**
     * Поиск СИЗ на складе
     *
     * @param keyword ключевое слово по которому осуществляется поиск
     * @param model   модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/not-issued-siz/search/stock-siz/{keyword}")
    public String searchStockSiz(@PathVariable(value = "keyword") String keyword, Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        List<SIZForStock> sizesForStock = new ArrayList<>();
        List<Object[]> objectList;
        if (role.getName().equals("ROLE_USER")) {    //если пользователь СуперЮзер то выводим общую информацию
            if (keyword.equals("0")) {
                objectList = issuedSIZRepository.findGroupbySizeAndHeight();
            } else {
                objectList = issuedSIZRepository.findGroupbySizeAndHeightByKeyword(keyword);
            }
        } else {      //иначе отображаем информацию по текущему подразделению
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (keyword.equals("0")) {
                objectList = issuedSIZRepository.findGroupbySizeAndHeightByKomplex(komplex.getId());
            } else {
                objectList = issuedSIZRepository.findGroupbySizeAndHeightByKomplexAndKeyword(komplex.getId(), keyword);
            }
        }
        for (Object[] obj : objectList) {
            sizesForStock.add(new SIZForStock(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], (String) obj[4], Integer.parseInt(obj[5].toString())));
        }
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("typeSIZS", individualProtectionMeans);
        model.addAttribute("notIssuedSIZ", sizesForStock);
        return "user/mto/siz/siz-from-stock :: table-sock-siz";
    }

    ///////////////// выданный СИЗ/////////////////

    /**
     * Первоначальная загрузка страницы выдачи СИЗ
     *
     * @param model модель аттрибутов страницы
     * @return веб страница
     */
    @GetMapping("/userPage/issued-siz")
    public String issuedSIZAll(Model model, Authentication authentication) {
        Post post = new Post("");
        Employee employee = new Employee("", "", "", "", "", null, null);
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("komplexs", komplexRepository.findAll());
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        if (role.getName().equals("ROLE_USER")) {
            employees = employeeRepository.findAll();
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            employees = employeeRepository.findAllByKomplexId(komplex.getId());
            model.addAttribute("komplex", komplex);
        }
        model.addAttribute("employees", employees);
        model.addAttribute("selectedEmployee", employee);
        model.addAttribute("selected", post);
        model.addAttribute("ipmStandardRepository", ipmStandardRepository);
        return "user/mto/siz/issued/issued-siz-add";
    }


    /**
     * Первоначальная загрузка страницы выдачи СИЗ
     *
     * @param model модель аттрибутов страницы
     * @return веб страница
     */
    @GetMapping("/userPage/issued-siz-manual")
    public String issuedSIZToEach(Model model, Authentication authentication) {
        Post post = new Post("");
        Employee employee = new Employee("", "", "", "", "", null, null);
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("komplexs", komplexRepository.findAll());
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        if (role.getName().equals("ROLE_USER")) {
            employees = employeeRepository.findAll();
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            employees = employeeRepository.findAllByKomplexId(komplex.getId());
            model.addAttribute("komplex", komplex);
        }
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("typeSIZS", individualProtectionMeans);
        model.addAttribute("employees", employees);
        model.addAttribute("selectedEmployee", employee);
        model.addAttribute("selected", post);
        return "user/mto/siz/issued/issued-siz-manual";
    }

    /**
     * Обновление таблицы сотрудников по выбранному подразделению
     *
     * @param id    ID подразделения
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-siz/getEmployeeForKomplex/{id}")
    public String getEmployeeForKomplex(@PathVariable(value = "id") long id, Model model) {
        List<Employee> employees = employeeRepository.findAllByKomplexId(id);
        model.addAttribute("employees", employees);
        return "user/mto/siz/issued/issued-siz-add :: table-employees";
    }

    /**
     * Обновление таблицы сотрудников по выбранной должности
     *
     * @param id    ID должности
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-siz/getEmployeeForPost/{id}")
    public String getEmployeeForPost(@PathVariable(value = "id") long id, Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")) {
            employees = employeeRepository.findAllByPostId(id);
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            employees = employeeRepository.findAllByKomplexIdAndPostId(komplex.getId(), id);
        }
        model.addAttribute("employees", employees);
        return "user/mto/siz/issued/issued-siz-add :: table-employees";
    }

    /**
     * Обновление таблицы сотрудников по подразделению и должности
     *
     * @param id_komplex ID подразделения
     * @param id_post    ID должности
     * @param model      модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-siz/getEmployeeForKomplexAndPost/{id_komplex}/{id_post}")
    public String getEmployeeForKomplexAndPost(@PathVariable(value = "id_komplex") long id_komplex, @PathVariable(value = "id_post") long id_post, Model model) {
        List<Employee> employees = employeeRepository.findAllByKomplexIdAndPostId(id_komplex, id_post);
        model.addAttribute("employees", employees);
        return "user/mto/siz/issued/issued-siz-add :: table-employees";
    }

    /**
     * Обновление таблицы Нормы СИЗ для выбранного сотрудника и должности
     *
     * @param id    ID должности
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-siz/getSizForEmployee/{id}")
    public String getSizForEmployee(@PathVariable(value = "id") long id, Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        if (!role.getName().equals("ROLE_USER")) {
            model.addAttribute("komplex", komplexRepository.findByRoleId(role.getId()));
        }
        Post post = postRepository.findByEmployeeId(id);
        List<IPMStandard> ipmStandards = ipmStandardRepository.findAllByPostId(post.getId());
        model.addAttribute("siz", ipmStandards);
        model.addAttribute("selected", post);
        model.addAttribute("ipmStandardRepository", ipmStandardRepository);
        return "user/mto/siz/issued/issued-siz-add :: table-siz";
    }

    /**
     * Обновление таблицы Нормы СИЗ для выбранной должности
     *
     * @param id             ID должности
     * @param authentication данные о пользователе
     * @param model          модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-siz/getSizForPost/{id}")
    public String getSizForPost(@PathVariable(value = "id") long id, Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        if (!role.getName().equals("ROLE_USER")) {
            model.addAttribute("komplex", komplexRepository.findByRoleId(role.getId()));
        }
        Post post = postRepository.findById(id).orElse(null);
        List<IPMStandard> ipmStandards = ipmStandardRepository.findAllByPostId(id);
        model.addAttribute("siz", ipmStandards);
        model.addAttribute("selected", post);
        model.addAttribute("ipmStandardRepository", ipmStandardRepository);
        return "user/mto/siz/issued/issued-siz-add :: table-siz";
    }

    /**
     * Обновление таблицы уже выданного СИЗ сотруднику
     *
     * @param id    ID сотрудника
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-siz/getIssuedSizForEmployee/{id}")
    public String getIssuedSizForEmployee(@PathVariable(value = "id") long id, Model model) {
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(id, "Выдано");
        Employee employee = employeeRepository.findById(id).orElse(null);
        employeeForIssuedSIZ = employee;
        model.addAttribute("selectedEmployee", employee);
        model.addAttribute("vidanSIZ", issuedSIZS);
        return "user/mto/siz/issued/issued-siz-add :: table-issuedSiz";
    }

    /**
     * Функция ручной выдачи СИЗ сотрудникам
     *
     * @param list  список ID сотрудников которым выдается СИЗ
     * @param typeSIZ_id ID типа выдаваемого СИЗ
     * @param size размер
     * @param height рост
     * @param number кол-во выдаваемого СИЗ
     * @param nomenclature номенклатурный номер
     * @param serviceLife срок носки СИЗ
     * @param datei дата выдачи СИЗ
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-siz/{list}/add/{id}/{size}/{height}/{number}/{nomenclature}/{serviceLife}/{dateissued}")
    public String addIssuedSizManual(@PathVariable(value = "list") List<Long> list,
                                     @PathVariable(value = "id") long typeSIZ_id,
                                     @PathVariable(value = "size") String size,
                                     @PathVariable(value = "height") String height,
                                     @PathVariable(value = "number") int number,
                                     @PathVariable(value = "nomenclature") String nomenclature,
                                     @PathVariable(value = "serviceLife") int serviceLife,
                                     @PathVariable(value = "dateissued") String datei, Model model) throws ParseException {
        IndividualProtectionMeans ipm = sizRepository.findById(typeSIZ_id).orElse(null);

        ArrayList<IssuedSIZ> listSIZs = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            if (height.equals("non")) {
                listSIZs.add(new IssuedSIZ(ipm, size, nomenclature));
            } else {
                listSIZs.add(new IssuedSIZ(ipm, size, height, nomenclature));
            }
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateIssued = format.parse(datei);

        Calendar c = Calendar.getInstance();
        c.setTime(dateIssued);
        c.add(Calendar.MONTH, serviceLife);
        Date dateEndWear = c.getTime();

        for (Long employee_id : list) {
            Employee employee = employeeRepository.findById(employee_id).orElse(null);
            for (int i = 0; i < number; i++) {
                IssuedSIZ siz = listSIZs.get(i);

                siz.setEmployee(employee);
                siz.setDateIssued(dateIssued);
                siz.setDateEndWear(dateEndWear);
                siz.setStatus("Выдано");
                siz.setKomplex(null);
                issuedSIZRepository.save(siz);
            }
        }

        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(), "Выдано");
        model.addAttribute("errors", listErrors);
        model.addAttribute("vidanSIZ", issuedSIZS2);
        model.addAttribute("selectedEmployee", employeeForIssuedSIZ);
        return "user/mto/siz/issued/issued-siz-add :: table-issuedSiz";
    }

    /**
     * Функция выдачи СИЗ сотрудникам
     *
     * @param list  список ID сотрудников которым выдается СИЗ
     * @param id    ID нормы выдачи
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-siz/{list}/add/{id}/{dateissued}")
    public String addIssuedSiz(@PathVariable(value = "list") List<Long> list,
                               @PathVariable(value = "id") long id,
                               @PathVariable(value = "dateissued") String datei, Model model) throws ParseException {
        String message = "";
        listErrors.clear();
        List<IssuedSIZ> issuedSIZS = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateIssued = format.parse(datei);
        IPMStandard ipmStandard = ipmStandardRepository.findById(id).orElse(null);
        int serviceLife = ipmStandard.getServiceLife();  //срок носки
        int number = ipmStandard.getIssuanceRate(); //норма выдачи
        Calendar c = Calendar.getInstance();
        c.setTime(dateIssued);
        c.add(Calendar.MONTH, serviceLife);
        Date dateEndWear = c.getTime();
        String typeSIZ = ipmStandard.getIndividualProtectionMeans().getTypeIPM();
        SIZForPrint.clear();

        for (Long employee_id : list) {
            message = "";
            Employee employee = employeeRepository.findById(employee_id).orElse(null);
            List<IssuedSIZ> employeesSIZ = issuedSIZRepository.findByEmployeeIdAndIPMStandart(id, employee_id);
            if (employeesSIZ.size() < number) {        //проверка выдано ли все СИЗ сотруднику
                switch (typeSIZ) {
                    case "Одежда":
                        issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForClothing(id, employee_id);
                        break;
                    case "Головной убор":
                        issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForHead(id, employee_id);
                        break;
                    case "Обувь":
                        issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForShoe(id, employee_id);
                        break;
                    case "Противогаз":
                        issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForGasMask(id, employee_id);
                        break;
                    case "Респиратор":
                        issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForRespirator(id, employee_id);
                        break;
                    case "Перчатки":
                        issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForGlove(id, employee_id);
                        break;
                    case "Рукавицы":
                        issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForMittens(id, employee_id);
                        break;
                    default:
                        message = "Для " + employee.getSurname() + " " + employee.getName() + " " + employee.getPatronymic() + " выбран несуществующий тип СИЗ";
                        break;
                }
                int issued_number = number - employeesSIZ.size();
                if ((issuedSIZS != null) && (issuedSIZS.size() > 0)) {
                    if (issuedSIZS.size() < issued_number) {
                        message = "Для " + employee.getSurname() + " " + employee.getName() + " " + employee.getPatronymic() + " СИЗ на складе не достаточно, выдано " + issuedSIZS.size() + " из " + issued_number + " запрошенных";
                    }
                    for (int i = 0; i < issued_number; i++) {
                        IssuedSIZ siz = issuedSIZS.get(i);

                        siz.setEmployee(employee);
                        siz.setDateIssued(dateIssued);
                        siz.setDateEndWear(dateEndWear);
                        siz.setStatus("Выдано");
                        siz.setKomplex(null);
                        issuedSIZRepository.save(siz);
                        SIZForPrint.add(siz);
                    }
                } else {
                    message = "Нужные размеры для " + employee.getSurname() + " " + employee.getName() + " " + employee.getPatronymic() + " на складе отсутствуют";
                }
            } else {
                message = "Для " + employee.getSurname() + " " + employee.getName() + " " + employee.getPatronymic() + " СИЗ данного типа был выдан ранее";
            }
            if (!message.equals("")) {
                listErrors.add(message);
            }
        }

        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(), "Выдано");
        model.addAttribute("errors", listErrors);
        model.addAttribute("vidanSIZ", issuedSIZS2);
        model.addAttribute("selectedEmployee", employeeForIssuedSIZ);
        model.addAttribute("issuedError", message);
        return "user/mto/siz/issued/issued-siz-add :: table-issuedSiz";
    }

    /**
     * Печать акта выдачи сотрудникам СИЗ
     */
    @PostMapping("/userPage/issued-siz/print-issued-siz")
    public void printPurchasingTable(HttpServletResponse response) throws IOException {
        //if (!SIZForPrint.isEmpty()) {
            DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=act_vidachi_siz_ot_" + dateFormatter.format(new Date()) + ".xlsx";
            response.setHeader(headerKey, headerValue);

            String excelFilePath = Paths.get("").toAbsolutePath().toString() + File.separator + "template" + File.separator + "akt-vidachi-siz.xlsx";

            try {
                FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
                Workbook workbook = WorkbookFactory.create(inputStream);

                Sheet sheet = workbook.getSheetAt(0);

                int row = 12;
                Row exampleRow = sheet.getRow(12);
                int count = 1;
                for (IssuedSIZ s : SIZForPrint) {
                    sheet.createRow(row+2);
                    sheet.getRow(row).setRowStyle(exampleRow.getRowStyle());
                    sheet.getRow(row).setHeight(exampleRow.getHeight());

                    for (int i = 0; i < 8; i++) {
                        sheet.getRow(row+1).createCell(i);
                        sheet.getRow(row).getCell(i).setCellStyle(exampleRow.getCell(i).getCellStyle());
                    }
                    sheet.getRow(row).getCell(0).setCellValue(count);
                    sheet.getRow(row).getCell(1).setCellValue(s.getNomenclatureNumber());
                    sheet.getRow(row).getCell(2).setCellValue(s.getSiz().getNameSIZ());
                    sheet.getRow(row).getCell(3).setCellValue(s.getSize());
                    sheet.getRow(row).getCell(4).setCellValue(s.getHeight());
                    sheet.getRow(row).getCell(5).setCellValue(s.getSiz().getEd_izm());
                    sheet.getRow(row).getCell(6).setCellValue(s.getEmployee().getSurname() + " " + s.getEmployee().getName().charAt(0) + " " + s.getEmployee().getPatronymic().charAt(0));
                    row++;
                    count++;

                }

                inputStream.close();

                ServletOutputStream outputStream = response.getOutputStream();
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();

            } catch (IOException | EncryptedDocumentException
                    ex) {
                ex.printStackTrace();
            }
        //}
    }

    /**
     * Отправка на страницу списка ошибок при выдаче СИЗ
     *
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-siz/errors")
    public String sendErrors(Model model) {
        model.addAttribute("errors", listErrors);
        return "user/mto/siz/issued/issued-siz-add :: table-errors";
    }

    /**
     * Продление ресурса по дате для выбранного СИЗ
     *
     * @param id            ID выданного СИЗ
     * @param dateExtending дата продления
     * @param model         модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-siz/{id}/extend/{dateExtending}")
    public String extendIssuedSiz(@PathVariable(value = "id") long id, @PathVariable(value = "dateExtending") String dateExtending, Model model) {
        String message = "";
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElse(null);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date exDate = issuedSIZ.getDateEndWear();
        try {
            exDate = format.parse(dateExtending);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        issuedSIZ.setDateEndWear(exDate);
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(), "Выдано");
        model.addAttribute("vidanSIZ", issuedSIZS2);
        model.addAttribute("selectedEmployee", employeeForIssuedSIZ);
        model.addAttribute("issuedError", message);
        return "user/mto/siz/issued/issued-siz-add :: table-issuedSiz";
    }

    /**
     * Отмена выдачи СИЗ
     *
     * @param id    ID выданного СИЗ
     * @param model модель аттрибутов страницы
     * @return фрагмент страницы
     */
    @GetMapping("/userPage/issued-siz/{id}/cancel")
    public String cancelIssuedSiz(@PathVariable(value = "id") long id, Model model) {
        String message = "";
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElse(null);
      //  issuedSIZ.setKomplex(issuedSIZ.getEmployee().getKomplex());
        issuedSIZ.setDateIssued(null);
        issuedSIZ.setDateEndWear(null);
        issuedSIZ.setEmployee(null);
        issuedSIZ.setStatus("На складе");
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(), "Выдано");
        model.addAttribute("vidanSIZ", issuedSIZS2);
        model.addAttribute("selectedEmployee", employeeForIssuedSIZ);
        model.addAttribute("issuedError", message);
        return "user/mto/siz/issued/issued-siz-add :: table-issuedSiz";
    }

    /**
     * Списание СИЗ
     *
     * @param id    ID выданного СИЗ
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-siz/{id}/writeoff/{actName}")
    public String writeOfIssuedSiz(@PathVariable(value = "id") long id, @PathVariable(value = "actName") String actName, Model model) {
        String message = "";
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElse(null);
        issuedSIZ.setStatus("Списано");
        issuedSIZ.setEmployee(null);
        issuedSIZ.setWriteOffAct(actName);
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(), "Выдано");
        model.addAttribute("vidanSIZ", issuedSIZS2);
        model.addAttribute("selectedEmployee", employeeForIssuedSIZ);
        model.addAttribute("issuedError", message);
        return "user/mto/siz/issued/issued-siz-add :: table-issuedSiz";
    }

    /**
     * Поиск сотрудников по ключевому слову
     *
     * @param keyword ключевое слово поиска
     * @param model   модель аттрибутов страницы
     * @return фрагмент страницы
     */
    @GetMapping("/userPage/issued-siz/search/employee/{keyword}")
    public String searchEmployee(@PathVariable(value = "keyword") String keyword, Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")) {
            if (keyword.equals("0")) {
                employees = employeeRepository.findAll();
            } else {
                employees = employeeRepository.findAllByPostAndKeyword(keyword);
            }
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (keyword.equals("0")) {
                employees = employeeRepository.findAllByKomplexId(komplex.getId());
            } else {
                employees = employeeRepository.findAllByPostAndKomplexIdAndKeyword(keyword, komplex.getId());
            }
        }
        model.addAttribute("employees", employees);
        return "user/mto/siz/issued/issued-siz-add :: table-employees";
    }


    //////////////////////Укомплектованность сотрудников СИЗ//////////////////////

    /**
     * Первоначальное открытие страницы укомплектованности СИЗ
     *
     * @param model модель аттрибутов страницы
     * @return веб страница
     */
    @GetMapping("/userPage/employee-siz")
    public String staffingOfAllEmployeesSIZ(Model model, Authentication authentication) {
        filerIssuedSizAll = "all";
        typeOfSortingEmployeeTable = new TypeOfSortingEmployeeTable();
        typeOfSortingEmployeeTable.setFilter("all");
        typeOfSortingEmployeeTable.setSearching("");
        String nextYearBegin = (Year.now().getValue() + 1) + "_01_01";
        String nextYearEnd = (Year.now().getValue() + 1) + "_12_31";
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        Iterable<Employee> fullStaffEmpl;
        Iterable<Employee> employeesEndingDateWear;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")) {
            employees = employeeRepository.findAll();
            fullStaffEmpl = employeeRepository.getFullStaffingOfEmployee();
            employeesEndingDateWear = employeeRepository.getEmployeesWithEndingDateWearForNextYear(nextYearBegin, nextYearEnd);
            model.addAttribute("komplexes", komplexRepository.findAll());
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            employees = employeeRepository.findAllByKomplexId(komplex.getId());
            fullStaffEmpl = employeeRepository.getFullStaffingOfEmployeeByKomplex(komplex.getId());
            employeesEndingDateWear = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKomplex(nextYearBegin, nextYearEnd, komplex.getId());
            model.addAttribute("komplex", komplex);
        }
        int countE = 0;
        for (Employee e : employees) {
            countE++;
        }
        int countFE = 0;
        for (Employee e : fullStaffEmpl) {
            countFE++;
        }
        int countEE = 0;
        for (Employee e : employeesEndingDateWear) {
            countEE++;
        }
        StatisticForStaffing info = new StatisticForStaffing(countE, countFE, countEE);
        model.addAttribute("info", info);
        model.addAttribute("employees", employees);
        model.addAttribute("employeeRepository", employeeRepository);
        model.addAttribute("ipmStandardRepository", issuedSIZRepository);
        model.addAttribute("employee", employees.iterator().next());
        return "user/mto/siz/issued/issued-siz-all";
    }

    /**
     * Фильтрация таблицы по подразделениям
     *
     * @param model      модель аттрибутов страницы
     * @param id_komplex ID выбранного комплекса
     * @return фрагмент таблицы
     */
    @GetMapping("/userPage/employee-siz/filter-by-komplex/{id_komplex}")
    public String staffingByKomplexOfAllEmployeesSIZ(Model model, @PathVariable(value = "id_komplex") long id_komplex) {
        filerIssuedSizAll = "all";
        typeOfSortingEmployeeTable = new TypeOfSortingEmployeeTable();
        typeOfSortingEmployeeTable.setFilter("all");
        typeOfSortingEmployeeTable.setSearching("");

        Iterable<Employee> employees;

        if (id_komplex == 0) {
            employees = employeeRepository.findAll();
        } else {
            employees = employeeRepository.findAllByKomplexId(id_komplex);
        }
        model.addAttribute("komplexes", komplexRepository.findAll());
        model.addAttribute("employees", employees);
        model.addAttribute("employeeRepository", employeeRepository);
        //model.addAttribute("employee", employees.iterator().next());
        return "user/mto/siz/issued/issued-siz-all :: table-employees";
    }

    /**
     * Обновление информации в информационном окне
     *
     * @param model      модель аттрибутов страницы
     * @param id_komplex ID выбранного комплекса
     * @return фрагмент информационного окна
     */
    @GetMapping("/userPage/employee-siz/update-info/{id_komplex}")
    public String updateInfoWindowOfAllEmployeesSIZ(Model model, @PathVariable(value = "id_komplex") long id_komplex) {
        String nextYearBegin = (Year.now().getValue() + 1) + "_01_01";
        String nextYearEnd = (Year.now().getValue() + 1) + "_12_31";
        Iterable<Employee> employees;
        Iterable<Employee> fullStaffEmpl;
        Iterable<Employee> employeesEndingDateWear;

        if (id_komplex == 0) {
            employees = employeeRepository.findAll();
            fullStaffEmpl = employeeRepository.getFullStaffingOfEmployee();
            employeesEndingDateWear = employeeRepository.getEmployeesWithEndingDateWearForNextYear(nextYearBegin, nextYearEnd);
        } else {
            employees = employeeRepository.findAllByKomplexId(id_komplex);
            fullStaffEmpl = employeeRepository.getFullStaffingOfEmployeeByKomplex(id_komplex);
            employeesEndingDateWear = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKomplex(nextYearBegin, nextYearEnd, id_komplex);
        }

        int countE = 0;
        for (Employee e : employees) {
            countE++;
        }
        int countFE = 0;
        for (Employee e : fullStaffEmpl) {
            countFE++;
        }
        int countEE = 0;
        for (Employee e : employeesEndingDateWear) {
            countEE++;
        }

        StatisticForStaffing info = new StatisticForStaffing(countE, countFE, countEE);
        model.addAttribute("info", info);
        return "user/mto/siz/issued/issued-siz-all :: info-window";
    }

    /**
     * Фильтрация сотрудников по укомплектованности
     *
     * @param keyword    ключевое слово поиска
     * @param id_komplex ID выбранного комплекса
     * @param model      модель аттрибутов страницы
     * @return фрагмент страницы
     */
    @GetMapping("/userPage/employee-siz/filter/employee/{keyword}/{id_komplex}")
    public String filterStaffingOfAllEmployeesSIZ(@PathVariable(value = "keyword") String keyword, @PathVariable(value = "id_komplex") long id_komplex, Authentication authentication, Model model) {
        filerIssuedSizAll = keyword;
        sortedByEndIssuedDate = false;
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")) {
            if (id_komplex != 0) { //фильтрация по выбранному комплексу
                if (keyword.equals("not-issued")) {
                    employees = employeeRepository.getNotFullStaffingOfEmployeeByKomlex(id_komplex);
                    typeOfSortingEmployeeTable.setFilter("not-issued");
                } else if (keyword.equals("issued")) {
                    employees = employeeRepository.getFullStaffingOfEmployeeByKomplex(id_komplex);
                    typeOfSortingEmployeeTable.setFilter("issued");
                } else {
                    employees = employeeRepository.findAllByKomplexId(id_komplex);
                    typeOfSortingEmployeeTable.setFilter("all");
                }
            } else {
                if (keyword.equals("not-issued")) {
                    employees = employeeRepository.getNotFullStaffingOfEmployee();
                    typeOfSortingEmployeeTable.setFilter("not-issued");
                } else if (keyword.equals("issued")) {
                    employees = employeeRepository.getFullStaffingOfEmployee();
                    typeOfSortingEmployeeTable.setFilter("issued");
                } else {
                    employees = employeeRepository.findAll();
                    typeOfSortingEmployeeTable.setFilter("all");
                }
            }
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (keyword.equals("not-issued")) {
                employees = employeeRepository.getNotFullStaffingOfEmployeeByKomlex(komplex.getId());
                typeOfSortingEmployeeTable.setFilter("not-issued");
            } else if (keyword.equals("issued")) {
                employees = employeeRepository.getFullStaffingOfEmployeeByKomplex(komplex.getId());
                typeOfSortingEmployeeTable.setFilter("issued");
            } else {
                employees = employeeRepository.findAllByKomplexId(komplex.getId());
                typeOfSortingEmployeeTable.setFilter("all");
            }
        }
        model.addAttribute("employees", employees);
        model.addAttribute("employeeRepository", employeeRepository);
        return "user/mto/siz/issued/issued-siz-all :: table-employees";
    }

    /**
     * Отображение сотрудников у которых срок носки СИЗ заканчивается в следующем году
     *
     * @param model      модель аттрибутов страницы
     * @param id_komplex ID выбранного комплекса
     * @return фрагмент
     */
    @GetMapping("/userPage/employee-siz/show-employees-with-end-wear-date/{id_komplex}")
    public String showEmployeesWithEndWearDate(Model model, @PathVariable(value = "id_komplex") long id_komplex, Authentication authentication) {
        typeOfSortingEmployeeTable.setFilter("end_date");
        String nextYearBegin = (Year.now().getValue() + 1) + "_01_01";
        String nextYearEnd = (Year.now().getValue() + 1) + "_12_31";
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")) {
            if (id_komplex != 0) {
                employees = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKomplex(nextYearBegin, nextYearEnd, id_komplex);
            } else {
                employees = employeeRepository.getEmployeesWithEndingDateWearForNextYear(nextYearBegin, nextYearEnd);
            }
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            employees = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKomplex(nextYearBegin, nextYearEnd, komplex.getId());
        }
        model.addAttribute("employees", employees);
        model.addAttribute("employeeRepository", employeeRepository);
        return "user/mto/siz/issued/issued-siz-all :: table-employees";
    }

    /**
     * Сортировка таблицы сотрудников по дате списания СИЗ
     *
     * @param model      модель аттрибутов страницы
     * @param id_komplex ID выбранного комплекса
     * @return фрагмент
     */
    @GetMapping("/userPage/employee-siz/sorting-date/employee/{id_komplex}")
    public String sortingByDateStaffingOfAllEmployeesSIZ(Model model, @PathVariable(value = "id_komplex") long id_komplex, Authentication authentication) {
        sortedByEndIssuedDate = true;
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")) {
            if (id_komplex != 0) {
                if (filerIssuedSizAll.equals("not-issued")) {
                    employees = employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(id_komplex);
                    typeOfSortingEmployeeTable.setFilter("not-issued-date");
                } else if (filerIssuedSizAll.equals("issued")) {
                    employees = employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(id_komplex);
                    typeOfSortingEmployeeTable.setFilter("issued-date");
                } else {
                    employees = employeeRepository.getStaffingOfEmployeeOrderByEndDateIssuedByKomplex(id_komplex);
                    typeOfSortingEmployeeTable.setFilter("date");
                }
            } else {
                if (filerIssuedSizAll.equals("not-issued")) {
                    employees = employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssued();
                    typeOfSortingEmployeeTable.setFilter("not-issued-date");
                } else if (filerIssuedSizAll.equals("issued")) {
                    employees = employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssued();
                    typeOfSortingEmployeeTable.setFilter("issued-date");
                } else {
                    employees = employeeRepository.getStaffingOfEmployeeOrderByEndDateIssued();
                    typeOfSortingEmployeeTable.setFilter("date");
                }
            }
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (filerIssuedSizAll.equals("not-issued")) {
                employees = employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(komplex.getId());
                typeOfSortingEmployeeTable.setFilter("not-issued-date");
            } else if (filerIssuedSizAll.equals("issued")) {
                employees = employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(komplex.getId());
                typeOfSortingEmployeeTable.setFilter("issued-date");
            } else {
                employees = employeeRepository.getStaffingOfEmployeeOrderByEndDateIssuedByKomplex(komplex.getId());
                typeOfSortingEmployeeTable.setFilter("date");
            }
        }
        model.addAttribute("employees", employees);
        model.addAttribute("employeeRepository", employeeRepository);
        return "user/mto/siz/issued/issued-siz-all :: table-employees";
    }

    /**
     * Поиск укомплектованных сотрудников по ключевому слову
     *
     * @param keyword    ключевое слово поиска
     * @param id_komplex ID выбранного комплекса
     * @param model      модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/employee-siz/search/employee/{keyword}/{id_komplex}")
    public String searchStaffingOfAllEmployeesSIZ(@PathVariable(value = "keyword") String keyword, @PathVariable(value = "id_komplex") long id_komplex, Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")) {
            if (id_komplex != 0) { // поиск по выбранному подразделению
                if (keyword.equals("0")) {
                    typeOfSortingEmployeeTable.setSearching("");
                    if (typeOfSortingEmployeeTable.getFilter().equals("end_date")) {
                        employees = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKomplex((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", id_komplex);
                    } else if (filerIssuedSizAll.equals("not-issued")) {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(id_komplex);
                            typeOfSortingEmployeeTable.setFilter("not-issued-date");
                        } else {
                            employees = employeeRepository.getNotFullStaffingOfEmployeeByKomlex(id_komplex);
                            typeOfSortingEmployeeTable.setFilter("not-issued");
                        }
                    } else if (filerIssuedSizAll.equals("issued")) {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(id_komplex);
                            typeOfSortingEmployeeTable.setFilter("issued-date");
                        } else {
                            employees = employeeRepository.getFullStaffingOfEmployeeByKomplex(id_komplex);
                            typeOfSortingEmployeeTable.setFilter("issued");
                        }
                    } else {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.getStaffingOfEmployeeOrderByEndDateIssuedByKomplex(id_komplex);
                            typeOfSortingEmployeeTable.setFilter("date");
                        } else {
                            employees = employeeRepository.findAllByKomplexId(id_komplex);
                            typeOfSortingEmployeeTable.setFilter("all");
                        }
                    }
                } else {
                    typeOfSortingEmployeeTable.setSearching(keyword);
                    if (typeOfSortingEmployeeTable.getFilter().equals("end_date")) {
                        employees = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKeywordAndKomplex((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", keyword, id_komplex);
                    } else if (filerIssuedSizAll.equals("not-issued")) {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.getNotFullStaffingOfEmployeeAndKeywordOrderByEndDateIssuedAndKomplex(keyword, id_komplex);
                            typeOfSortingEmployeeTable.setFilter("not-issued-date");
                        } else {
                            employees = employeeRepository.getNotFullStaffingOfEmployeeAndKeywordAndKomplex(keyword, id_komplex);
                            typeOfSortingEmployeeTable.setFilter("not-issued");
                        }
                    } else if (filerIssuedSizAll.equals("issued")) {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.getFullStaffingOfEmployeeAndKeywordOrderByEndDateIssuedAndKomplex(keyword, id_komplex);
                            typeOfSortingEmployeeTable.setFilter("issued-date");
                        } else {
                            employees = employeeRepository.getFullStaffingOfEmployeeAndKeywordAndKomplex(keyword, id_komplex);
                            typeOfSortingEmployeeTable.setFilter("issued");
                        }
                    } else {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.findAllByPostAndKomplexAndKeywordOrderByEndDateIssuedAndKomplex(keyword, id_komplex);
                            typeOfSortingEmployeeTable.setFilter("date");
                        } else {
                            employees = employeeRepository.findAllByPostAndKomplexIdAndKeyword(keyword, id_komplex);
                            typeOfSortingEmployeeTable.setFilter("all");
                        }
                    }
                }

            } else {
                if (keyword.equals("0")) {
                    typeOfSortingEmployeeTable.setSearching("");
                    if (typeOfSortingEmployeeTable.getFilter().equals("end_date")) {
                        employees = employeeRepository.getEmployeesWithEndingDateWearForNextYear((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31");
                    } else if (filerIssuedSizAll.equals("not-issued")) {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssued();
                            typeOfSortingEmployeeTable.setFilter("not-issued-date");
                        } else {
                            employees = employeeRepository.getNotFullStaffingOfEmployee();
                            typeOfSortingEmployeeTable.setFilter("not-issued");
                        }
                    } else if (filerIssuedSizAll.equals("issued")) {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssued();
                            typeOfSortingEmployeeTable.setFilter("issued-date");
                        } else {
                            employees = employeeRepository.getFullStaffingOfEmployee();
                            typeOfSortingEmployeeTable.setFilter("issued");
                        }
                    } else {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.getStaffingOfEmployeeOrderByEndDateIssued();
                            typeOfSortingEmployeeTable.setFilter("date");
                        } else {
                            employees = employeeRepository.findAll();
                            typeOfSortingEmployeeTable.setFilter("all");
                        }
                    }
                } else {
                    typeOfSortingEmployeeTable.setSearching(keyword);
                    if (typeOfSortingEmployeeTable.getFilter().equals("end_date")) {
                        employees = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKeyword((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", keyword);
                    } else if (filerIssuedSizAll.equals("not-issued")) {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.getNotFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(keyword);
                            typeOfSortingEmployeeTable.setFilter("not-issued-date");
                        } else {
                            employees = employeeRepository.getNotFullStaffingOfEmployeeAndKeyword(keyword);
                            typeOfSortingEmployeeTable.setFilter("not-issued");
                        }
                    } else if (filerIssuedSizAll.equals("issued")) {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.getFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(keyword);
                            typeOfSortingEmployeeTable.setFilter("issued-date");
                        } else {
                            employees = employeeRepository.getFullStaffingOfEmployeeAndKeyword(keyword);
                            typeOfSortingEmployeeTable.setFilter("issued");
                        }
                    } else {
                        if (sortedByEndIssuedDate) {
                            employees = employeeRepository.findAllByPostAndKomplexAndKeywordOrderByEndDateIssued(keyword);
                            typeOfSortingEmployeeTable.setFilter("date");
                        } else {
                            employees = employeeRepository.findAllByPostAndKomplexAndKeyword(keyword);
                            typeOfSortingEmployeeTable.setFilter("all");
                        }
                    }
                }
            }
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (keyword.equals("0")) {
                typeOfSortingEmployeeTable.setSearching("");
                if (typeOfSortingEmployeeTable.getFilter().equals("end_date")) {
                    employees = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKomplex((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", komplex.getId());
                } else if (filerIssuedSizAll.equals("not-issued")) {
                    if (sortedByEndIssuedDate) {
                        employees = employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("not-issued-date");
                    } else {
                        employees = employeeRepository.getNotFullStaffingOfEmployeeByKomlex(komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("not-issued");
                    }
                } else if (filerIssuedSizAll.equals("issued")) {
                    if (sortedByEndIssuedDate) {
                        employees = employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("issued-date");
                    } else {
                        employees = employeeRepository.getFullStaffingOfEmployeeByKomplex(komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("issued");
                    }
                } else {
                    if (sortedByEndIssuedDate) {
                        employees = employeeRepository.getStaffingOfEmployeeOrderByEndDateIssuedByKomplex(komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("date");
                    } else {
                        employees = employeeRepository.findAllByKomplexId(komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("all");
                    }
                }
            } else {
                typeOfSortingEmployeeTable.setSearching(keyword);
                if (typeOfSortingEmployeeTable.getFilter().equals("end_date")) {
                    employees = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKeywordAndKomplex((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", keyword, komplex.getId());
                } else if (filerIssuedSizAll.equals("not-issued")) {
                    if (sortedByEndIssuedDate) {
                        employees = employeeRepository.getNotFullStaffingOfEmployeeAndKeywordOrderByEndDateIssuedAndKomplex(keyword, komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("not-issued-date");
                    } else {
                        employees = employeeRepository.getNotFullStaffingOfEmployeeAndKeywordAndKomplex(keyword, komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("not-issued");
                    }
                } else if (filerIssuedSizAll.equals("issued")) {
                    if (sortedByEndIssuedDate) {
                        employees = employeeRepository.getFullStaffingOfEmployeeAndKeywordOrderByEndDateIssuedAndKomplex(keyword, komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("issued-date");
                    } else {
                        employees = employeeRepository.getFullStaffingOfEmployeeAndKeywordAndKomplex(keyword, komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("issued");
                    }
                } else {
                    if (sortedByEndIssuedDate) {
                        employees = employeeRepository.findAllByPostAndKomplexAndKeywordOrderByEndDateIssuedAndKomplex(keyword, komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("date");
                    } else {
                        employees = employeeRepository.findAllByPostAndKomplexIdAndKeyword(keyword, komplex.getId());
                        typeOfSortingEmployeeTable.setFilter("all");
                    }
                }
            }
        }
        model.addAttribute("employees", employees);
        model.addAttribute("employeeRepository", employeeRepository);
        return "user/mto/siz/issued/issued-siz-all :: table-employees";
    }

    /**
     * Обновление таблицы укомплектованности СИЗ для выбранного сотрудника
     *
     * @param id_e  ID сотрудника
     * @param id_p  ID должности
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/employee-siz/info-siz/employee/{id_e}/{id_p}")
    public String getInfoStaffingOfEmployee(@PathVariable(value = "id_e") long id_e, @PathVariable(value = "id_p") long id_p, Model model) {
        List<IPMStandard> ipmStandards = ipmStandardRepository.findAllByPostId(id_p);
        Employee employee = employeeRepository.findById(id_e).orElse(null);
        model.addAttribute("siz", ipmStandards);
        model.addAttribute("employee", employee);
        model.addAttribute("issuedSIZRepository", issuedSIZRepository);
        return "user/mto/siz/issued/issued-siz-all :: table-siz";
    }

    /**
     * Обновление таблицы с информацией об уже выданном СИЗ выбранного сотрудника
     *
     * @param id    ID сотрудника
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/employee-siz/info-issued-siz/employee/{id}")
    public String getInfoIssuedSizOfEmployee(@PathVariable(value = "id") long id, Model model) {
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(id, "Выдано");
        Employee employee = employeeRepository.findById(id).orElse(null);
        model.addAttribute("employee", employee);
        model.addAttribute("vidanSIZ", issuedSIZS);
        return "user/mto/siz/issued/issued-siz-all :: table-issued-siz";
    }
////////////////////////////////redaktirovanie vidannogo siz sotrudniku

    /**
     * Открытие страницы редактирования укомплектованности СИЗ выбранного сотрудника
     *
     * @param id    ID сотрудника
     * @param model модель аттрибутов страницы
     * @return веб страница
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/employee/{id}")
    public String getEditStaffingPageOfEmployee(@PathVariable(value = "id") long id, Authentication authentication, Model model) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        employeeForIssuedSIZ = employee;
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(id, "Выдано");
        List<IPMStandard> ipmStandards = ipmStandardRepository.findAllByPostId(employee.getPost().getId());

        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        if (!role.getName().equals("ROLE_USER")) {
            model.addAttribute("komplex", komplexRepository.findByRoleId(role.getId()));
        }

        model.addAttribute("siz", ipmStandards);
        model.addAttribute("employee", employee);
        model.addAttribute("vidanSIZ", issuedSIZS);
        model.addAttribute("ipmStandardRepository", ipmStandardRepository);
        return "user/mto/siz/issued/issued-siz-edit";
    }

    /**
     * Продление ресурса СИЗ для сотрудника
     *
     * @param id            ID выданного СИЗ
     * @param dateExtending дата продления носки
     * @param model         модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/{id}/extend/{dateExtending}")
    public String extendIssuedSizForEmployee(@PathVariable(value = "id") long id, @PathVariable(value = "dateExtending") String dateExtending, Model model) {
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElse(null);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date exDate = issuedSIZ.getDateEndWear();
        try {
            exDate = format.parse(dateExtending);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        issuedSIZ.setDateEndWear(exDate);
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(), "Выдано");
        model.addAttribute("employee", employeeForIssuedSIZ);
        model.addAttribute("vidanSIZ", issuedSIZS2);
        return "user/mto/siz/issued/issued-siz-edit :: table-issuedSiz";
    }

    /**
     * Отмена выдачи СИЗ сотрудника
     *
     * @param id    ID выданного СИЗ
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/{id}/cancel")
    public String cancelIssuedSizForEmployee(@PathVariable(value = "id") long id, Model model) {
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElse(null);
        issuedSIZ.setKomplex(issuedSIZ.getEmployee().getKomplex());
        issuedSIZ.setDateIssued(null);
        issuedSIZ.setDateEndWear(null);
        issuedSIZ.setEmployee(null);
        issuedSIZ.setStatus("На складе");
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(), "Выдано");
        model.addAttribute("employee", employeeForIssuedSIZ);
        model.addAttribute("vidanSIZ", issuedSIZS2);
        return "user/mto/siz/issued/issued-siz-edit :: table-issuedSiz";
    }

    /**
     * Списание выданного СИЗ сотрудника
     *
     * @param id      ID выданного СИЗ
     * @param actName № акта списания
     * @param model   модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/{id}/writeoff/{actName}")
    public String writeOfIssuedSizForEmployee(@PathVariable(value = "id") long id, @PathVariable(value = "actName") String actName, Model model) {
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElse(null);
        issuedSIZ.setStatus("Списано");
        issuedSIZ.setWriteOffAct(actName);
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(), "Выдано");
        model.addAttribute("employee", employeeForIssuedSIZ);
        model.addAttribute("vidanSIZ", issuedSIZS2);
        return "user/mto/siz/issued/issued-siz-edit :: table-issuedSiz";
    }

    /**
     * Функция выдачи СИЗ сотруднику на странице комплектации
     *
     * @param id    ID нормы выдачи
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/employee/add/{id}")
    public String addIssuedSizForEmployee(@PathVariable(value = "id") long id, Model model) {
        String message = "";
        List<IssuedSIZ> issuedSIZS = null;
        Date dateIssued = new Date();
        IPMStandard ipmStandard = ipmStandardRepository.findById(id).orElse(null);
        int serviceLife = ipmStandard.getServiceLife();  //срок носки
        int number = ipmStandard.getIssuanceRate(); //норма выдачи
        Calendar c = Calendar.getInstance();
        c.setTime(dateIssued);
        c.add(Calendar.MONTH, serviceLife);
        Date dateEndWear = c.getTime();
        String typeSIZ = ipmStandard.getIndividualProtectionMeans().getTypeIPM();

        Long employee_id = employeeForIssuedSIZ.getId();
        message = "";
        Employee employee = employeeRepository.findById(employee_id).orElse(null);
        List<IssuedSIZ> employeesSIZ = issuedSIZRepository.findByEmployeeIdAndIPMStandart(id, employee_id);
        if (employeesSIZ.size() < number) {        //проверка выдано ли все СИЗ сотруднику
            switch (typeSIZ) {
                case "Одежда":
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForClothing(id, employee_id);
                    break;
                case "Головной убор":
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForHead(id, employee_id);
                    break;
                case "Обувь":
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForShoe(id, employee_id);
                    break;
                case "Противогаз":
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForGasMask(id, employee_id);
                    break;
                case "Респиратор":
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForRespirator(id, employee_id);
                    break;
                case "Перчатки":
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForGlove(id, employee_id);
                    break;
                case "Рукавицы":
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForMittens(id, employee_id);
                    break;
                default:
                    message = "Выбран несуществующий тип СИЗ";
                    break;
            }
            if ((issuedSIZS != null) && (issuedSIZS.size() > 0)) {
                int issued_number = number - employeesSIZ.size();
                if (issuedSIZS.size() < issued_number) {
                    message = "Для " + employeeForIssuedSIZ.getSurname() + " " + employeeForIssuedSIZ.getName() + " СИЗ на складе не достаточно, выдано " + issuedSIZS.size() + " из " + issued_number + " запрошенных";
                }
                for (int i = 0; i < issued_number; i++) {
                    IssuedSIZ siz = issuedSIZS.get(i);
                    siz.setEmployee(employeeForIssuedSIZ);
                    siz.setDateIssued(dateIssued);
                    siz.setDateEndWear(dateEndWear);
                    siz.setKomplex(null);
                    siz.setStatus("Выдано");
                    issuedSIZRepository.save(siz);
                }
            } else {
                message = "Нужные размеры для " + employeeForIssuedSIZ.getSurname() + " " + employeeForIssuedSIZ.getName() + " на складе отсутствуют";
            }
        } else {
            System.out.println("Для " + employeeForIssuedSIZ.getSurname() + " " + employeeForIssuedSIZ.getName() + " СИЗ выдан");
        }
        System.out.println(message);

        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(), "Выдано");
        model.addAttribute("vidanSIZ", issuedSIZS2);
        model.addAttribute("employee", employeeForIssuedSIZ);
        model.addAttribute("issuedError", message);
        return "user/mto/siz/issued/issued-siz-edit :: table-issuedSiz";
    }

    /**
     * Обновление таблицы с нормами выдачи СИЗ для сотрудника
     *
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/employee/update-norms")
    public String updateSizNormsForEmployee(Model model) {
        Post post = postRepository.findByEmployeeId(employeeForIssuedSIZ.getId());
        List<IPMStandard> ipmStandards = ipmStandardRepository.findAllByPostId(post.getId());
        model.addAttribute("siz", ipmStandards);
        model.addAttribute("employee", employeeForIssuedSIZ);
        model.addAttribute("ipmStandardRepository", ipmStandardRepository);
        return "user/mto/siz/issued/issued-siz-edit :: table-siz";
    }

    /**
     * Печать списка укомплектованности сотрудников
     *
     * @param response       http данные со страницы
     * @param id_komplex ID выбранного подразделения
     * @param authentication информация о пользователе
     * @throws IOException выброс исключения в случае невозможности отправки на страницу потока с данными
     */
    @GetMapping("/userPage/employee-siz/print-table/{id_komplex}")
    public void printTableEmployee(HttpServletResponse response, @PathVariable(value = "id_komplex") long id_komplex, Authentication authentication) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("dd_MM_yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=spisok_ukomplektovannosti_sotrudnikov_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<EmployeeForPrint> listEmployeeForPrint = new ArrayList<>();
        List<Employee> listEmployee;
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")) {
            if (id_komplex != 0){
                listEmployee = (List<Employee>) employeeRepository.findAllByKomplexId(id_komplex);
                if (typeOfSortingEmployeeTable.getFilter().equals("all") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.findAllByPostAndKomplexIdAndKeyword(typeOfSortingEmployeeTable.getSearching(), id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeByKomlex(id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeAndKeywordAndKomplex(typeOfSortingEmployeeTable.getSearching(), id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("issued") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeByKomplex(id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("issued") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeAndKeywordAndKomplex(typeOfSortingEmployeeTable.getSearching(), id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getStaffingOfEmployeeOrderByEndDateIssuedByKomplex(id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.findAllByPostAndKomplexAndKeywordOrderByEndDateIssuedAndKomplex(typeOfSortingEmployeeTable.getSearching(), id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued-date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued-date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeAndKeywordOrderByEndDateIssuedAndKomplex(typeOfSortingEmployeeTable.getSearching(), id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("issued-date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("issued-date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeAndKeywordOrderByEndDateIssuedAndKomplex(typeOfSortingEmployeeTable.getSearching(), id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("end_date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getEmployeesWithEndingDateWearForNextYearByKomplex((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", id_komplex);
                } else if (typeOfSortingEmployeeTable.getFilter().equals("end_date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getEmployeesWithEndingDateWearForNextYearByKeywordAndKomplex((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", typeOfSortingEmployeeTable.getSearching(), id_komplex);
                }
            } else {
                listEmployee = (List<Employee>) employeeRepository.findAll();
                if (typeOfSortingEmployeeTable.getFilter().equals("all") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.findAllByPostAndKomplexAndKeyword(typeOfSortingEmployeeTable.getSearching());
                } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployee();
                } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeAndKeyword(typeOfSortingEmployeeTable.getSearching());
                } else if (typeOfSortingEmployeeTable.getFilter().equals("issued") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployee();
                } else if (typeOfSortingEmployeeTable.getFilter().equals("issued") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeAndKeyword(typeOfSortingEmployeeTable.getSearching());
                } else if (typeOfSortingEmployeeTable.getFilter().equals("date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getStaffingOfEmployeeOrderByEndDateIssued();
                } else if (typeOfSortingEmployeeTable.getFilter().equals("date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.findAllByPostAndKomplexAndKeywordOrderByEndDateIssued(typeOfSortingEmployeeTable.getSearching());
                } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued-date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssued();
                } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued-date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(typeOfSortingEmployeeTable.getSearching());
                } else if (typeOfSortingEmployeeTable.getFilter().equals("issued-date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssued();
                } else if (typeOfSortingEmployeeTable.getFilter().equals("issued-date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(typeOfSortingEmployeeTable.getSearching());
                } else if (typeOfSortingEmployeeTable.getFilter().equals("end_date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getEmployeesWithEndingDateWearForNextYear((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31");
                } else if (typeOfSortingEmployeeTable.getFilter().equals("end_date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                    listEmployee = (List<Employee>) employeeRepository.getEmployeesWithEndingDateWearForNextYearByKeyword((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", typeOfSortingEmployeeTable.getSearching());
                }
            }
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            listEmployee = (List<Employee>) employeeRepository.findAllByKomplexId(komplex.getId());
            if (typeOfSortingEmployeeTable.getFilter().equals("all") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.findAllByPostAndKomplexIdAndKeyword(typeOfSortingEmployeeTable.getSearching(), komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeByKomlex(komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeAndKeywordAndKomplex(typeOfSortingEmployeeTable.getSearching(), komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("issued") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeByKomplex(komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("issued") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeAndKeywordAndKomplex(typeOfSortingEmployeeTable.getSearching(), komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.getStaffingOfEmployeeOrderByEndDateIssuedByKomplex(komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.findAllByPostAndKomplexAndKeywordOrderByEndDateIssuedAndKomplex(typeOfSortingEmployeeTable.getSearching(), komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued-date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("not-issued-date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeAndKeywordOrderByEndDateIssuedAndKomplex(typeOfSortingEmployeeTable.getSearching(), komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("issued-date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("issued-date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeAndKeywordOrderByEndDateIssuedAndKomplex(typeOfSortingEmployeeTable.getSearching(), komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("end_date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.getEmployeesWithEndingDateWearForNextYearByKomplex((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", komplex.getId());
            } else if (typeOfSortingEmployeeTable.getFilter().equals("end_date") && !typeOfSortingEmployeeTable.getSearching().equals("")) {
                listEmployee = (List<Employee>) employeeRepository.getEmployeesWithEndingDateWearForNextYearByKeywordAndKomplex((Year.now().getValue() + 1) + "_01_01", (Year.now().getValue() + 1) + "_12_31", typeOfSortingEmployeeTable.getSearching(), komplex.getId());
            }
        }

        for (Employee e : listEmployee) {
            listEmployeeForPrint.add(new EmployeeForPrint(e, issuedSIZRepository.getByEndWearDateForEmployee(e.getId()), employeeRepository.getPercentStaffingOfEmployee(e.getId(), e.getPost().getId())));
        }
        EmployeeStaffingExcelExporter excelExporter = new EmployeeStaffingExcelExporter(listEmployeeForPrint);
        excelExporter.export(response);
    }

    /**
     * Отображение таблицы с историей выдачи СИЗ выбранного сотрудника
     *
     * @param id    ID сотрудника
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/employee/history-issued-siz/{id}")
    public String getHistoryInfoIssuedSizOfEmployee(@PathVariable(value = "id") long id, Model model) {
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(id, "Списано");
        Employee employee = employeeRepository.findById(id).orElse(null);
        model.addAttribute("employee", employee);
        model.addAttribute("vidanSIZ", issuedSIZS);
        return "user/mto/siz/issued/issued-siz-edit :: table-history-issued-siz";
    }

    /**
     * Первоначальное открытие страницы по выдаче СИЗ подразделениям
     *
     * @param model модель аттрибутов страницы
     * @return веб страница
     */
    @GetMapping("/userPage/issued-for-komplex")
    public String getIssuedForKomplexSIZ(Model model) {
        model.addAttribute("komplexes", komplexRepository.findAll());
        return "user/mto/siz/issued/issued-siz-komplex";
    }

    /**
     * Отображение на странице СИЗ для передачи со склада на выбранное подразделение
     *
     * @param id    ID подразделения
     * @param model модель аттрибутов страницы
     * @return фрагмент
     */
    @GetMapping("/userPage/issued-for-komplex/get/{id}")
    public String getIssuedForKomplexSIZ(@PathVariable(value = "id") long id, Model model) {
        List<SIZForPurchase> sizesForKomplex = new ArrayList<>();
        List<Object[]> objectList = issuedSIZRepository.getIssuedSIZForKomplex(id);
        for (Object[] obj : objectList) {
            sizesForKomplex.add(new SIZForPurchase(Long.parseLong(obj[0].toString()), (String) obj[1], (String) obj[2], (String) obj[3], Integer.parseInt(obj[4].toString())));
        }
        model.addAttribute("siz", sizesForKomplex);
        model.addAttribute("issuedSIZRepository", issuedSIZRepository);
        return "user/mto/siz/issued/issued-siz-komplex :: table-siz";
    }

    /**
     * Получение списка СИЗ и печать акта приема-передачи в выбранное подразделение
     *
     * @param objectList список СИЗ передаваемого с общего склада на склад выбранного подразделения
     * @param response   http данные со страницы
     * @param id         ID подразделения
     */
    @PostMapping("/userPage/issued-for-komplex/send/{id}")
    public void sendSIZToKomplex(@RequestBody List<SIZForKomplex> objectList, HttpServletResponse response, @PathVariable(value = "id") long id) throws IOException {
        Komplex komplex = komplexRepository.findById(id).orElse(null);
        StringBuilder message = new StringBuilder();
        for (SIZForKomplex obj : objectList) {
            List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findBySizeAndHeightAndSizIdAndStatusAndKomplexIdAndEmployeeId(obj.getSize(), obj.getHeight(), obj.getId(), "На складе", null, null);
            if (issuedSIZS.size() >= obj.getNumber()) {
                for (int i = 0; i < obj.getNumber(); i++) {
                    IssuedSIZ siz = issuedSIZS.get(0);
                    siz.setKomplex(komplex);
                    issuedSIZRepository.save(siz);
                    issuedSIZS.remove(0);
                }
            } else {
                int size = issuedSIZS.size();
                message.append("На складе ").append(obj.getNomenclatureNumber()).append(" ").append(obj.getNamesiz()).append(" рост: ").append(obj.getHeight()).append(" размер: ").append(obj.getSize()).append(" не достаточно\n").append("в подазделение отправлено ").append(size).append(" из запрошеных ").append(obj.getNumber()).append("\n");
                for (int i = 0; i < size; i++) {
                    IssuedSIZ siz = issuedSIZS.get(0);
                    siz.setKomplex(komplex);
                    issuedSIZRepository.save(siz);
                    issuedSIZS.remove(0);
                }
            }
        }

        DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        Transliterator toLatinTrans = Transliterator.getInstance("Russian-Latin/BGN");
        String headerValue = "attachment; filename=act_priema_peredachi_podrazdeleniyu_" + toLatinTrans.transliterate(komplex.getShortName()) + "_ot_" + dateFormatter.format(new Date()) + ".xlsx";
        response.setHeader(headerKey, headerValue);

        String excelFilePath = Paths.get("").toAbsolutePath().toString() + File.separator + "template" + File.separator + "akt-priema-peredachi-siz.xlsx";

        try {
            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            int row = 12;
            Row exampleRow = sheet.getRow(12);
            int count = 1;
            for (SIZForKomplex s : objectList) {
                if (s.getNumber() != 0) {
                    sheet.getRow(row).setRowStyle(exampleRow.getRowStyle());
                    sheet.getRow(row).setHeight(exampleRow.getHeight());

                    for (int i = 0; i < 7; i++) {
                        sheet.getRow(row).getCell(i).setCellStyle(exampleRow.getCell(i).getCellStyle());
                    }
                    sheet.getRow(row).getCell(0).setCellValue(count);
                    sheet.getRow(row).getCell(1).setCellValue(s.getNamesiz());
                    sheet.getRow(row).getCell(2).setCellValue(s.getSize());
                    sheet.getRow(row).getCell(3).setCellValue(s.getHeight());
                    sheet.getRow(row).getCell(4).setCellValue(s.getNumber());
                    sheet.getRow(row).getCell(5).setCellValue(sizRepository.findById(s.getId()).orElse(null).getEd_izm());
                    sheet.getRow(row).getCell(6).setCellValue(" ");
                    row++;
                    count++;
                }
            }

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

}
