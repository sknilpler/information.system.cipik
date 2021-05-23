package com.information.system.cipik.controllers;

import com.information.system.cipik.models.*;
import com.information.system.cipik.repo.*;
import com.information.system.cipik.utils.EmployeeForPrint;
import com.information.system.cipik.utils.EmployeeStaffingExcelExporter;
import com.information.system.cipik.utils.StatisticForStaffing;
import com.information.system.cipik.utils.TypeOfSortingEmployeeTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    ItemsRepository itemsRepository;
    @Autowired
    SizeSizRepository sizeSizRepository;

    private Iterable<Post> listPosts;
    private Post postAddToNorm;
    private Post postToIssued;
    private Employee employeeForIssuedSIZ;
    private String filerIssuedSizAll;
    private boolean sortedByEndIssuedDate;
    private TypeOfSortingEmployeeTable typeOfSortingEmployeeTable;

    @GetMapping("/userPage/siz-types")
    public String allSIZ(Model model) {
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("ipms", individualProtectionMeans);
        return "user/mto/siz/types-of-siz";
    }


    @PostMapping("/userPage/siz-types")
    public String addSIZ(@RequestParam String nameSIZ, @RequestParam String ed_izm, @RequestParam String typeIPM, @RequestParam String nomenclatureNumber, Model model) {
        IndividualProtectionMeans individualProtectionMeans = new IndividualProtectionMeans(nameSIZ, ed_izm, typeIPM, nomenclatureNumber);
        sizRepository.save(individualProtectionMeans);
        return "redirect:/userPage/siz-types";
    }

    @GetMapping("/userPage/siz-types/{id}/edit")
    public String editSIZ(@PathVariable(value = "id") long id, Model model) {
        if (!sizRepository.existsById(id)) {
            return "redirect:/userPage/siz-types";
        }
        IndividualProtectionMeans individualProtectionMeans = sizRepository.findById(id).orElseThrow();
        model.addAttribute("siz", individualProtectionMeans);
        return "user/mto/siz/types-of-siz-edit";
    }

    @PostMapping("/userPage/siz-types/{id}/edit")
    public String updateSIZ(@PathVariable(value = "id") long id, @RequestParam String nameSIZ, @RequestParam String ed_izm, @RequestParam String typeIPM, @RequestParam String nomenclatureNumber, Model model) {
        IndividualProtectionMeans individualProtectionMeans = sizRepository.findById(id).orElseThrow();
        individualProtectionMeans.setNameSIZ(nameSIZ);
        individualProtectionMeans.setNomenclatureNumber(nomenclatureNumber);
        individualProtectionMeans.setEd_izm(ed_izm);
        individualProtectionMeans.setTypeIPM(typeIPM);
        sizRepository.save(individualProtectionMeans);
        return "redirect:/userPage/siz-types";
    }

    @PostMapping("/userPage/siz-types/{id}/remove")
    public String deleteSIZ(@PathVariable(value = "id") long id, Model model) {
        IndividualProtectionMeans individualProtectionMeans = sizRepository.findById(id).orElseThrow();
        sizRepository.deleteById(id);
        return "redirect:/userPage/siz-types";
    }

    //////////////////нормы СИЗ//////////////////////

    @GetMapping("/userPage/siz/norms")
    public String normsAll(Model model, String keyword) {
        Iterable<IPMStandard> normas = null;
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
        model.addAttribute("norms", normas);

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

    @GetMapping("/userPage/siz/norms/getNormsForPost/{id}")
    public String normsForPost(@PathVariable(value = "id") long id, Model model) {
        Iterable<IPMStandard> normas = ipmStandardRepository.findAllByPostId(id);
        postAddToNorm = postRepository.findById(id).orElseThrow();
        model.addAttribute("norms", normas);
        model.addAttribute("posts",listPosts);
        model.addAttribute("selected", postAddToNorm);
        return "user/mto/siz/norms/siz-norms";
    }

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

    @PostMapping("/userPage/siz/norms/add")
    public String normsAdding(@RequestParam Long dropSIZ, @RequestParam int issuanceRate,
                              @RequestParam int serviceLife, @RequestParam String regulatoryDocuments, Model model) {
        if (!postAddToNorm.getPostName().equals("")) {
            IndividualProtectionMeans siz = sizRepository.findById(dropSIZ).orElseThrow();
            IPMStandard norma = new IPMStandard(postAddToNorm, siz, issuanceRate,serviceLife,regulatoryDocuments);
            ipmStandardRepository.save(norma);
        }
        return "redirect:/userPage/siz/norms";
    }

    @GetMapping("/userPage/siz/norms/{id}/edit")
    public String normsEdit(@PathVariable(value = "id") long id, Model model) {
        if (!ipmStandardRepository.existsById(id)) {
            return "redirect:/userPage/siz/norms";
        }
        IPMStandard norma = ipmStandardRepository.findById(id).orElseThrow();
        model.addAttribute("sizs",sizRepository.findAll());
        model.addAttribute("post",postAddToNorm);
        model.addAttribute("norma", norma);
        return "user/mto/siz/norms/siz-norms-edit";
    }

    @PostMapping("/userPage/siz/norms/{id}/edit")
    public String normsUpdate(@PathVariable(value = "id") long id, @RequestParam Long dropSIZ, @RequestParam int issuanceRate,
                              @RequestParam int serviceLife, @RequestParam String regulatoryDocuments, Model model) {
        IndividualProtectionMeans siz = sizRepository.findById(dropSIZ).orElseThrow();
        IPMStandard norma = ipmStandardRepository.findById(id).orElseThrow();
        norma.setIndividualProtectionMeans(siz);
        norma.setIssuanceRate(issuanceRate);
        norma.setServiceLife(serviceLife);
        norma.setRegulatoryDocuments(regulatoryDocuments);
        //norma.setRashodniki(rashodnikiRepository.findById(dropRash).orElseThrow());
        ipmStandardRepository.save(norma);
        return "redirect:/userPage/siz/norms";
    }

    @PostMapping("/userPage/siz/norms/{id}/remove")
    public String normsDelete(@PathVariable(value = "id") long id, Model model) {
       IPMStandard norma = ipmStandardRepository.findById(id).orElseThrow();
        ipmStandardRepository.deleteById(id);
        return "redirect:/userPage/siz/norms";
    }

    ////////////////СИЗ на складе///////////////////

    /**
     * Первоначальное открытие страницы склада СИЗ
     * @param model
     * @return
     */
    @GetMapping("/userPage/not-issued-siz")
    public String notIssuedSIZAll(Model model, Authentication authentication) {

        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        List<IssuedSIZ> issuedSIZS;
        if (role.getName().equals("ROLE_USER")) {
            issuedSIZS = issuedSIZRepository.findByStatus("На складе");
        } else {  //иначе определяем подразделение пользователя и по нему выводим информацию
            // userKomplex = komplexRepository.findByRoleId(role.getId());
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            issuedSIZS = issuedSIZRepository.findByStatusAndKomplexId("На складе", komplex.getId());
        }
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("typeSIZS", individualProtectionMeans);
        model.addAttribute("notIssuedSIZ", issuedSIZS);
        return "user/mto/siz/siz-from-stock";
    }

    /**
     * Добавление нового СИЗ
     * @param typeSIZ
     * @param size
     * @param height
     * @param model
     * @return
     */
    @PostMapping("/userPage/not-issued-siz")
    public String notIssuedSIZAdd(@RequestParam Long typeSIZ, @RequestParam String size, @RequestParam String height, @RequestParam int number, Model model) {
        IndividualProtectionMeans ipm = sizRepository.findById(typeSIZ).orElseThrow();
        IssuedSIZ issuedSIZ =null;
        for (int i = 0; i < number; i++) {
        if (height.equals("non")) {
            issuedSIZ = new IssuedSIZ(ipm,size);
        }else{
            issuedSIZ = new IssuedSIZ(ipm,size,height);
        }
            issuedSIZRepository.save(issuedSIZ);
        }
       return  "redirect:/userPage/not-issued-siz";
    }

    /**
     * Редактирование данных о СИЗ
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/not-issued-siz/{id}/edit")
    public String notIssuedSIZEdit(@PathVariable(value = "id") long id, Model model){
        if (!issuedSIZRepository.existsById(id)) {
            return "redirect:/userPage/not-issued-siz";
        }
        IssuedSIZ siz = issuedSIZRepository.findById(id).orElseThrow();
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("typeSIZS",individualProtectionMeans);
        model.addAttribute("siz",siz);
        return "user/mto/siz/siz-from-stock-edit";
    }

    /**
     * Сохранение отредактированных данных о СИЗ
     * @param id
     * @param siz
     * @param size
     * @param height
     * @param model
     * @return
     */
    @PostMapping("/userPage/not-issued-siz/{id}/edit")
    public String notIssuedSIZUpdate(@PathVariable(value = "id") long id,@RequestParam IndividualProtectionMeans siz, @RequestParam String size, @RequestParam String height, Model model){
        IssuedSIZ isiz = issuedSIZRepository.findById(id).orElseThrow();
        isiz.setSiz(siz);
        isiz.setSize(size);
        isiz.setHeight(height);
        issuedSIZRepository.save(isiz);
        return "redirect:/userPage/not-issued-siz";
    }

    /**
     * Удаление выбранного СИЗ
     * @param list
     * @param model
     * @return
     */
    @PostMapping("/userPage/not-issued-siz/{list}/remove")
    public String notIssuedSIZDelete(@PathVariable(value = "list") List<Long> list,Authentication authentication, Model model){
//определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        List<IssuedSIZ> issuedSIZS;
        if (role.getName().equals("ROLE_USER")){    //если пользователь СуперЮзер то просто удаляем записи из базы
            for (Long id : list) {
                issuedSIZRepository.deleteById(id);
            }
            issuedSIZS = issuedSIZRepository.findByStatus("На складе");
        }else{      //иначе возвращаем СИЗ на главный склад центра
            for (Long id : list) {
               IssuedSIZ siz = issuedSIZRepository.findById(id).orElseThrow();
               siz.setKomplex(null);
               issuedSIZRepository.save(siz);
            }
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            issuedSIZS = issuedSIZRepository.findByStatusAndKomplexId("На складе", komplex.getId());
        }
        model.addAttribute("notIssuedSIZ", issuedSIZS);
        return "user/mto/siz/siz-from-stock :: table-sock-siz";
    }

    /**
     * Поиск СИЗ на складе
     * @param keyword
     * @param model
     * @return
     */
    @GetMapping("/userPage/not-issued-siz/search/stock-siz/{keyword}")
    public String searchStockSiz(@PathVariable(value = "keyword") String keyword,Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        List<IssuedSIZ> issuedSIZS;
        if (role.getName().equals("ROLE_USER")){    //если пользователь СуперЮзер то выводим общую информацию
            if (keyword.equals("0")) {
                issuedSIZS = issuedSIZRepository.findByStatus("На складе");
            } else {
                issuedSIZS = issuedSIZRepository.findByStatusAndSizeLikeOrHeightLikeOrSizNameSIZLike("На складе","%"+keyword+"%","%"+keyword+"%", "%"+keyword+"%");
            }
        }else{      //иначе отображаем информацию по текущему подразделению
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (keyword.equals("0")) {
                issuedSIZS = issuedSIZRepository.findByStatusAndKomplexId("На складе", komplex.getId());
            } else {
                issuedSIZS = issuedSIZRepository.findByStatusAndKomplexIdAndSizeLikeOrHeightLikeOrSizNameSIZLike("На складе", komplex.getId(), "%"+keyword+"%","%"+keyword+"%", "%"+keyword+"%");
            }
        }
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("typeSIZS", individualProtectionMeans);
        model.addAttribute("notIssuedSIZ", issuedSIZS);
        return "user/mto/siz/siz-from-stock :: table-sock-siz";
    }

    ///////////////// выданный СИЗ/////////////////

    /**
     * Первоначальная загрузка страницы выдачи СИЗ
     * @param model
     * @param keyword
     * @return
     */
    @GetMapping("/userPage/issued-siz")
    public String issuedSIZAll(Model model, String keyword,Authentication authentication) {
        Post post = new Post("");
        Employee employee = new Employee("","","","","",null,null);
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("komplexs", komplexRepository.findAll());
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        if (role.getName().equals("ROLE_USER")){
            employees = employeeRepository.findAll();
        }else{  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            employees = employeeRepository.findAllByKomplexId(komplex.getId());
        }
        model.addAttribute("employees", employees);
        model.addAttribute("selectedEmployee",employee);
        model.addAttribute("selected", post);
        model.addAttribute("ipmStandardRepository", ipmStandardRepository);
        return "user/mto/siz/issued/issued-siz-add";
    }

    /**
     * Обновление таблицы сотрудников по выбранному подразделению
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/getEmployeeForKomplex/{id}")
    public String getEmployeeForKomplex(@PathVariable(value = "id") long id, Model model) {
        List<Employee> employees = employeeRepository.findAllByKomplexId(id);
        model.addAttribute("employees", employees);
        return "user/mto/siz/issued/issued-siz-add :: table-employees";
    }

    /**
     * Обновление таблицы сотрудников по выбранной должности
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/getEmployeeForPost/{id}")
    public String getEmployeeForPost(@PathVariable(value = "id") long id,Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")){
            employees = employeeRepository.findAllByPostId(id);
        }else{  //иначе определяем подразделение пользователя и по нему выводим информацию
            // userKomplex = komplexRepository.findByRoleId(role.getId());
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            employees = employeeRepository.findAllByKomplexIdAndPostId(komplex.getId(),id);
        }
        model.addAttribute("employees", employees);
        return "user/mto/siz/issued/issued-siz-add :: table-employees";
    }

    /**
     * Обновление таблицы сотрудников по подразделению и должности
     * @param id_komplex
     * @param id_post
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/getEmployeeForKomplexAndPost/{id_komplex}/{id_post}")
    public String getEmployeeForKomplexAndPost(@PathVariable(value = "id_komplex") long id_komplex, @PathVariable(value = "id_post") long id_post, Model model) {
        List<Employee> employees = employeeRepository.findAllByKomplexIdAndPostId(id_komplex,id_post);
        model.addAttribute("employees", employees);
        return "user/mto/siz/issued/issued-siz-add :: table-employees";
    }

    /**
     * Обновление таблицы Нормы СИЗ для выбранного сотрудника и должности
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/getSizForEmployee/{id}")
    public String getSizForEmployee(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findByEmployeeId(id);
        List<IPMStandard> ipmStandards = ipmStandardRepository.findAllByPostId(post.getId());
        model.addAttribute("siz", ipmStandards);
        model.addAttribute("selected", post);
        model.addAttribute("ipmStandardRepository", ipmStandardRepository);
        return "user/mto/siz/issued/issued-siz-add :: table-siz";
    }

    /**
     * Обновление таблицы Нормы СИЗ для выбранной должности
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/getSizForPost/{id}")
    public String getSizForPost(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        List<IPMStandard> ipmStandards = ipmStandardRepository.findAllByPostId(id);
        model.addAttribute("siz", ipmStandards);
        model.addAttribute("selected", post);
        model.addAttribute("ipmStandardRepository", ipmStandardRepository);
        return "user/mto/siz/issued/issued-siz-add :: table-siz";
    }

    /**
     * Обновление таблицы уже выданного СИЗ сотруднику
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/getIssuedSizForEmployee/{id}")
    public String getIssuedSizForEmployee(@PathVariable(value = "id") long id, Model model) {
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(id,"Выдано");
        Employee employee = employeeRepository.findById(id).orElseThrow();
        employeeForIssuedSIZ = employee;
        model.addAttribute("selectedEmployee",employee);
        model.addAttribute("vidanSIZ",issuedSIZS);
        return "user/mto/siz/issued/issued-siz-add :: table-issuedSiz";
    }

    /**
     * Функция выдачи СИЗ сотруднику
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/{list}/add/{id}")
    public String addIssuedSiz(@PathVariable(value = "list") List<Long> list, @PathVariable(value = "id") long id, Model model) {
        String message = "";
        List<IssuedSIZ> issuedSIZS = null;
        Date dateIssued = new Date();
        IPMStandard ipmStandard = ipmStandardRepository.findById(id).orElseThrow();
        int serviceLife = ipmStandard.getServiceLife();  //срок носки
        int number = ipmStandard.getIssuanceRate(); //норма выдачи
        Calendar c = Calendar.getInstance();
        c.setTime(dateIssued);
        c.add(Calendar.MONTH, serviceLife);
        Date dateEndWear = c.getTime();
        String typeSIZ = ipmStandard.getIndividualProtectionMeans().getTypeIPM();

        for (Long employee_id: list) {
            message = "";
            Employee employee = employeeRepository.findById(employee_id).orElseThrow();
            List<IssuedSIZ> employeesSIZ = issuedSIZRepository.findByEmployeeIdAndIPMStandart(id, employee_id);
            if(employeesSIZ.size()<number) {        //проверка выдано ли все СИЗ сотруднику
                if (typeSIZ.equals("Одежда")) {
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForClothing(id, employee_id);
                } else if (typeSIZ.equals("Головной убор")) {
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForHead(id, employee_id);
                } else if (typeSIZ.equals("Обувь")) {
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForShoe(id, employee_id);
                } else if (typeSIZ.equals("Противогаз")) {
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForGasMask(id, employee_id);
                } else if (typeSIZ.equals("Респиратор")) {
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForRespirator(id, employee_id);
                } else if (typeSIZ.equals("Перчатки")) {
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForGlove(id, employee_id);
                } else if (typeSIZ.equals("Рукавицы")) {
                    issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForMittens(id, employee_id);
                } else {
                    message = "Выбран несуществующий тип СИЗ";
                }
                int issued_number = number-employeesSIZ.size();
                if ((issuedSIZS != null) && (issuedSIZS.size() > 0)) {
                    if (issuedSIZS.size() < issued_number) {
                        message = "Для " + employee.getSurname() + " " + employee.getName() + " СИЗ на складе не достаточно, выдано " + issuedSIZS.size() + " из " + issued_number + " запрошенных";
                    }
                    for (int i = 0; i < issued_number; i++) {
                        IssuedSIZ siz = issuedSIZS.get(i);

                        siz.setEmployee(employee);
                        siz.setDateIssued(dateIssued);
                        siz.setDateEndWear(dateEndWear);
                        siz.setStatus("Выдано");
                        siz.setKomplex(null);
                        issuedSIZRepository.save(siz);
                    }
                } else {
                    message = "Нужные размеры для " + employee.getSurname() + " " + employee.getName() + " на складе отсутствуют";
                }
            } else {
                System.out.println("Для " + employee.getSurname() + " " + employee.getName() + " СИЗ выдан");
            }
            System.out.println(message);
        }

        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(),"Выдано");
        model.addAttribute("vidanSIZ", issuedSIZS2);
        model.addAttribute("selectedEmployee", employeeForIssuedSIZ);
        model.addAttribute("issuedError", message);
        return "user/mto/siz/issued/issued-siz-add :: table-issuedSiz";
    }

    /**
     * Продление ресурса по дате для выбранного СИЗ
     * @param id
     * @param dateExtending
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/{id}/extend/{dateExtending}")
    public String extendIssuedSiz(@PathVariable(value = "id") long id, @PathVariable(value = "dateExtending") String dateExtending, Model model) {
        String message = "";
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElseThrow();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date exDate = issuedSIZ.getDateEndWear();
        try {
            exDate = format.parse(dateExtending);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        issuedSIZ.setDateEndWear(exDate);
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(),"Выдано");
        model.addAttribute("vidanSIZ", issuedSIZS2);
        model.addAttribute("selectedEmployee", employeeForIssuedSIZ);
        model.addAttribute("issuedError", message);
        return "user/mto/siz/issued/issued-siz-add :: table-issuedSiz";
    }

    /**
     * Отмена выдачи СИЗ
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/{id}/cancel")
    public String cancelIssuedSiz(@PathVariable(value = "id") long id, Model model) {
        String message = "";
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElseThrow();
        issuedSIZ.setKomplex(issuedSIZ.getEmployee().getKomplex());
        issuedSIZ.setDateIssued(null);
        issuedSIZ.setDateEndWear(null);
        issuedSIZ.setEmployee(null);
        issuedSIZ.setStatus("На складе");
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(),"Выдано");
        model.addAttribute("vidanSIZ", issuedSIZS2);
        model.addAttribute("selectedEmployee", employeeForIssuedSIZ);
        model.addAttribute("issuedError", message);
        return "user/mto/siz/issued/issued-siz-add :: table-issuedSiz";
    }

    /**
     * Списание СИЗ
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/{id}/writeoff/{actName}")
    public String writeOfIssuedSiz(@PathVariable(value = "id") long id, @PathVariable(value = "actName") String actName, Model model) {
        String message = "";
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElseThrow();
        issuedSIZ.setStatus("Списано");
        issuedSIZ.setEmployee(null);
        issuedSIZ.setWriteOffAct(actName);
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(),"Выдано");
        model.addAttribute("vidanSIZ", issuedSIZS2);
        model.addAttribute("selectedEmployee", employeeForIssuedSIZ);
        model.addAttribute("issuedError", message);
        return "user/mto/siz/issued/issued-siz-add :: table-issuedSiz";
    }

    /**
     * Поиск сотрудников по ключевому слову
     * @param keyword
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/search/employee/{keyword}")
    public String searchEmployee(@PathVariable(value = "keyword") String keyword,Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")){
            if (keyword.equals("0")){
                employees = employeeRepository.findAll();
            }else{
                employees = employeeRepository.findAllByPostAndKeyword(keyword);
            }
        }else{  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (keyword.equals("0")){
                employees = employeeRepository.findAllByKomplexId(komplex.getId());
            }else{
                employees = employeeRepository.findAllByPostAndKomplexIdAndKeyword(keyword, komplex.getId());
            }
        }
        model.addAttribute("employees", employees);
        return "user/mto/siz/issued/issued-siz-add :: table-employees";
    }



    //////////////////////Укомплектованность сотрудников СИЗ//////////////////////

    /**
     * Первоначальное открытие страницы укомплектованности СИЗ
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz")
    public String staffingOfAllEmployeesSIZ(Model model,Authentication authentication) {
        filerIssuedSizAll = "all";
        typeOfSortingEmployeeTable = new TypeOfSortingEmployeeTable();
        typeOfSortingEmployeeTable.setFilter("all");
        typeOfSortingEmployeeTable.setSearching("");
        String nextYearBegin = (Year.now().getValue()+1)+"_01_01";
        String nextYearEnd = (Year.now().getValue()+1)+"_12_31";
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        Iterable<Employee> fullStaffEmpl;
        Iterable<Employee> employeesEndingDateWear;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")){
            employees = employeeRepository.findAll();
            fullStaffEmpl = employeeRepository.getFullStaffingOfEmployee();
            employeesEndingDateWear = employeeRepository.getEmployeesWithEndingDateWearForNextYear(nextYearBegin,nextYearEnd);
        }else{  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            employees = employeeRepository.findAllByKomplexId(komplex.getId());
            fullStaffEmpl = employeeRepository.getFullStaffingOfEmployeeByKomplex(komplex.getId());
            employeesEndingDateWear = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKomplex(nextYearBegin,nextYearEnd, komplex.getId());
        }
        int countE = 0;
        for (Employee e:employees) {
            countE++;
        }
        int countFE = 0;
        for (Employee e:fullStaffEmpl) {
            countFE++;
        }
        int countEE = 0;
        for (Employee e:employeesEndingDateWear) {
            countEE++;
        }
        StatisticForStaffing info = new StatisticForStaffing(countE,countFE,countEE);
        model.addAttribute("info",info);
        model.addAttribute("employees",employees);
        model.addAttribute("employeeRepository",employeeRepository);
        model.addAttribute("ipmStandardRepository", issuedSIZRepository);
        model.addAttribute("employee", employees.iterator().next());
        return "user/mto/siz/issued/issued-siz-all";
    }

    /**
     * Фильтрация сотрудников по укомплектованности
     * @param keyword
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/filter/employee/{keyword}")
    public String filterStaffingOfAllEmployeesSIZ(@PathVariable(value = "keyword") String keyword,Authentication authentication, Model model) {
        filerIssuedSizAll = keyword;
        sortedByEndIssuedDate = false;
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")){
            if (keyword.equals("not-issued")){
                employees = employeeRepository.getNotFullStaffingOfEmployee();
                typeOfSortingEmployeeTable.setFilter("not-issued");
            } else if (keyword.equals("issued")){
                employees = employeeRepository.getFullStaffingOfEmployee();
                typeOfSortingEmployeeTable.setFilter("issued");
            } else {
                employees = employeeRepository.findAll();
                typeOfSortingEmployeeTable.setFilter("all");
            }
        }else{  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (keyword.equals("not-issued")){
                employees = employeeRepository.getNotFullStaffingOfEmployeeByKomlex(komplex.getId());
                typeOfSortingEmployeeTable.setFilter("not-issued");
            } else if (keyword.equals("issued")){
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
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/show-employees-with-end-wear-date")
    public String showEmployeesWithEndWearDate(Model model, Authentication authentication){
        typeOfSortingEmployeeTable.setFilter("end_date");
        String nextYearBegin = (Year.now().getValue()+1)+"_01_01";
        String nextYearEnd = (Year.now().getValue()+1)+"_12_31";
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")){
            employees = employeeRepository.getEmployeesWithEndingDateWearForNextYear(nextYearBegin,nextYearEnd);
        }else{  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            employees = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKomplex(nextYearBegin,nextYearEnd, komplex.getId());
        }
        model.addAttribute("employees", employees);
        model.addAttribute("employeeRepository", employeeRepository);
        return "user/mto/siz/issued/issued-siz-all :: table-employees";
    }

    /**
     * Сортировка таблицы сотрудников по дате списания СИЗ
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/sorting-date/employee")
    public String sortingByDateStaffingOfAllEmployeesSIZ(Model model,Authentication authentication) {
        sortedByEndIssuedDate = true;
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")){
            if (filerIssuedSizAll.equals("not-issued")){
                employees = employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssued();
                typeOfSortingEmployeeTable.setFilter("not-issued-date");
            } else if (filerIssuedSizAll.equals("issued")){
                employees = employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssued();
                typeOfSortingEmployeeTable.setFilter("issued-date");
            } else {
                employees = employeeRepository.getStaffingOfEmployeeOrderByEndDateIssued();
                typeOfSortingEmployeeTable.setFilter("date");
            }
        }else{  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (filerIssuedSizAll.equals("not-issued")){
                employees = employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(komplex.getId());
                typeOfSortingEmployeeTable.setFilter("not-issued-date");
            } else if (filerIssuedSizAll.equals("issued")){
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
     * @param keyword
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/search/employee/{keyword}")
    public String searchStaffingOfAllEmployeesSIZ(@PathVariable(value = "keyword") String keyword,Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")){
            if (keyword.equals("0")) {
                typeOfSortingEmployeeTable.setSearching("");
                if (typeOfSortingEmployeeTable.getFilter().equals("end_date")){
                    employees = employeeRepository.getEmployeesWithEndingDateWearForNextYear((Year.now().getValue()+1)+"_01_01",(Year.now().getValue()+1)+"_12_31");
                }else if (filerIssuedSizAll.equals("not-issued")) {
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
                if (typeOfSortingEmployeeTable.getFilter().equals("end_date")){
                    employees = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKeyword((Year.now().getValue()+1)+"_01_01",(Year.now().getValue()+1)+"_12_31",keyword);
                }else if (filerIssuedSizAll.equals("not-issued")) {
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
        }else{  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (keyword.equals("0")) {
                typeOfSortingEmployeeTable.setSearching("");
                if (typeOfSortingEmployeeTable.getFilter().equals("end_date")){
                    employees = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKomplex((Year.now().getValue()+1)+"_01_01",(Year.now().getValue()+1)+"_12_31", komplex.getId());
                }else if (filerIssuedSizAll.equals("not-issued")) {
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
                if (typeOfSortingEmployeeTable.getFilter().equals("end_date")){
                    employees = employeeRepository.getEmployeesWithEndingDateWearForNextYearByKeyword((Year.now().getValue()+1)+"_01_01",(Year.now().getValue()+1)+"_12_31",keyword);
                }else if (filerIssuedSizAll.equals("not-issued")) {
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

        model.addAttribute("employees", employees);
        model.addAttribute("employeeRepository", employeeRepository);
        return "user/mto/siz/issued/issued-siz-all :: table-employees";
    }

    /**
     * Обновление таблицы укомплектованности СИЗ для выбранного сотрудника
     * @param id_e
     * @param id_p
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/info-siz/employee/{id_e}/{id_p}")
    public String getInfoStaffingOfEmployee(@PathVariable(value = "id_e") long id_e,@PathVariable(value = "id_p") long id_p, Model model) {
        List<IPMStandard> ipmStandards = ipmStandardRepository.findAllByPostId(id_p);
        Employee employee = employeeRepository.findById(id_e).orElseThrow();
        model.addAttribute("siz", ipmStandards);
        model.addAttribute("employee", employee);
        model.addAttribute("issuedSIZRepository", issuedSIZRepository);
        return "user/mto/siz/issued/issued-siz-all :: table-siz";
    }

    /**
     * Обновление таблицы с информацией об уже выданном СИЗ выбранного сотрудника
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/info-issued-siz/employee/{id}")
    public String getInfoIssuedSizOfEmployee(@PathVariable(value = "id") long id, Model model) {
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(id,"Выдано");
        Employee employee = employeeRepository.findById(id).orElseThrow();
        model.addAttribute("employee",employee);
        model.addAttribute("vidanSIZ",issuedSIZS);
        return "user/mto/siz/issued/issued-siz-all :: table-issued-siz";
    }
////////////////////////////////redaktirovanie vidannogo siz sotrudniku
    /**
     * Открытие страницы редактирования укомплектованности СИЗ выбранного сотрудника
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/employee/{id}")
    public String getEditStaffingPageOfEmployee(@PathVariable(value = "id") long id, Model model) {
        Employee employee = employeeRepository.findById(id).orElseThrow();
        employeeForIssuedSIZ = employee;
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(id,"Выдано");
        List<IPMStandard> ipmStandards = ipmStandardRepository.findAllByPostId(employee.getPost().getId());
        model.addAttribute("siz", ipmStandards);
        model.addAttribute("employee",employee);
        model.addAttribute("vidanSIZ",issuedSIZS);
        model.addAttribute("ipmStandardRepository", ipmStandardRepository);
        return "user/mto/siz/issued/issued-siz-edit";
    }

    /**
     * Продление ресурса СИЗ для сотрудника
     * @param id
     * @param dateExtending
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/{id}/extend/{dateExtending}")
    public String extendIssuedSizForEmployee(@PathVariable(value = "id") long id, @PathVariable(value = "dateExtending") String dateExtending, Model model) {
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElseThrow();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date exDate = issuedSIZ.getDateEndWear();
        try {
            exDate = format.parse(dateExtending);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        issuedSIZ.setDateEndWear(exDate);
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(),"Выдано");
        model.addAttribute("employee",employeeForIssuedSIZ);
        model.addAttribute("vidanSIZ", issuedSIZS2);
        return "user/mto/siz/issued/issued-siz-edit :: table-issuedSiz";
    }

    /**
     * Отмена выдачи СИЗ сотрудника
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/{id}/cancel")
    public String cancelIssuedSizForEmployee(@PathVariable(value = "id") long id, Model model) {
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElseThrow();
        issuedSIZ.setDateIssued(null);
        issuedSIZ.setDateEndWear(null);
        issuedSIZ.setEmployee(null);
        issuedSIZ.setStatus("На складе");
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(),"Выдано");
        model.addAttribute("employee",employeeForIssuedSIZ);
        model.addAttribute("vidanSIZ", issuedSIZS2);
        return "user/mto/siz/issued/issued-siz-edit :: table-issuedSiz";
    }

    /**
     * Списание выданного СИЗ сотрудника
     * @param id
     * @param actName
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/{id}/writeoff/{actName}")
    public String writeOfIssuedSizForEmployee(@PathVariable(value = "id") long id, @PathVariable(value = "actName") String actName, Model model) {
        IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElseThrow();
        issuedSIZ.setStatus("Списано");
        issuedSIZ.setWriteOffAct(actName);
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(),"Выдано");
        model.addAttribute("employee",employeeForIssuedSIZ);
        model.addAttribute("vidanSIZ", issuedSIZS2);
        return "user/mto/siz/issued/issued-siz-edit :: table-issuedSiz";
    }

    /**
     * Функция выдачи СИЗ сотруднику на странице комплектации
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/employee/add/{id}")
    public String addIssuedSizForEmployee(@PathVariable(value = "id") long id, Model model) {
        String message = "";
        List<IssuedSIZ> issuedSIZS = null;
        Date dateIssued = new Date();
        IPMStandard ipmStandard = ipmStandardRepository.findById(id).orElseThrow();
        int serviceLife = ipmStandard.getServiceLife();  //срок носки
        int number = ipmStandard.getIssuanceRate(); //норма выдачи
        Calendar c = Calendar.getInstance();
        c.setTime(dateIssued);
        c.add(Calendar.MONTH, serviceLife);
        Date dateEndWear = c.getTime();
        String typeSIZ = ipmStandard.getIndividualProtectionMeans().getTypeIPM();

        Long employee_id = employeeForIssuedSIZ.getId();
        message = "";
        Employee employee = employeeRepository.findById(employee_id).orElseThrow();
        List<IssuedSIZ> employeesSIZ = issuedSIZRepository.findByEmployeeIdAndIPMStandart(id, employee_id);
        if (employeesSIZ.size() < number) {        //проверка выдано ли все СИЗ сотруднику
            if (typeSIZ.equals("Одежда")) {
                issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForClothing(id, employee_id);
            } else if (typeSIZ.equals("Головной убор")) {
                issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForHead(id, employee_id);
            } else if (typeSIZ.equals("Обувь")) {
                issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForShoe(id, employee_id);
            } else if (typeSIZ.equals("Противогаз")) {
                issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForGasMask(id, employee_id);
            } else if (typeSIZ.equals("Респиратор")) {
                issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForRespirator(id, employee_id);
            } else if (typeSIZ.equals("Перчатки")) {
                issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForGlove(id, employee_id);
            } else if (typeSIZ.equals("Рукавицы")) {
                issuedSIZS = issuedSIZRepository.findNotIssuedByIPMStandartForMittens(id, employee_id);
            } else {
                message = "Выбран несуществующий тип СИЗ";
            }
            if ((issuedSIZS != null) && (issuedSIZS.size() > 0)) {
                int issued_number = number-employeesSIZ.size();
                if (issuedSIZS.size() < issued_number) {
                    message = "Для " + employeeForIssuedSIZ.getSurname() + " " + employeeForIssuedSIZ.getName() + " СИЗ на складе не достаточно, выдано " + issuedSIZS.size() + " из " + issued_number + " запрошенных";
                }
                for (int i = 0; i < issued_number; i++) {
                    IssuedSIZ siz = issuedSIZS.get(i);
                    siz.setEmployee(employeeForIssuedSIZ);
                    siz.setDateIssued(dateIssued);
                    siz.setDateEndWear(dateEndWear);
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

        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(employeeForIssuedSIZ.getId(),"Выдано");
        model.addAttribute("vidanSIZ", issuedSIZS2);
        model.addAttribute("employee", employeeForIssuedSIZ);
        model.addAttribute("issuedError", message);
        return "user/mto/siz/issued/issued-siz-edit :: table-issuedSiz";
    }

    /**
     * Обновление таблицы с нормами выдачи СИЗ для сотрудника
     * @param model
     * @return
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

    @GetMapping("/userPage/employee-siz/print-table")
    public void printTableEmployee (HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("dd_MM_yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=spisok_ukomplektovannosti_sotrudnikov_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<EmployeeForPrint> listEmployeeForPrint = new ArrayList<>();
        List<Employee> listEmployee = (List<Employee>) employeeRepository.findAll();
        if (typeOfSortingEmployeeTable.getFilter().equals("all") && !typeOfSortingEmployeeTable.getSearching().equals("")){
            listEmployee = (List<Employee>) employeeRepository.findAllByPostAndKomplexAndKeyword(typeOfSortingEmployeeTable.getSearching());
        }else if(typeOfSortingEmployeeTable.getFilter().equals("not-issued") && typeOfSortingEmployeeTable.getSearching().equals("")){
            listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployee();
        } else if(typeOfSortingEmployeeTable.getFilter().equals("not-issued") && !typeOfSortingEmployeeTable.getSearching().equals("")){
             listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeAndKeyword(typeOfSortingEmployeeTable.getSearching());
        } else if(typeOfSortingEmployeeTable.getFilter().equals("issued") && typeOfSortingEmployeeTable.getSearching().equals("")){
            listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployee();
        } else if(typeOfSortingEmployeeTable.getFilter().equals("issued") && !typeOfSortingEmployeeTable.getSearching().equals("")){
             listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeAndKeyword(typeOfSortingEmployeeTable.getSearching());
        } else if(typeOfSortingEmployeeTable.getFilter().equals("date") && typeOfSortingEmployeeTable.getSearching().equals("")){
             listEmployee = (List<Employee>) employeeRepository.getStaffingOfEmployeeOrderByEndDateIssued();
        } else if(typeOfSortingEmployeeTable.getFilter().equals("date") && !typeOfSortingEmployeeTable.getSearching().equals("")){
            listEmployee = (List<Employee>) employeeRepository.findAllByPostAndKomplexAndKeywordOrderByEndDateIssued(typeOfSortingEmployeeTable.getSearching());
        } else if(typeOfSortingEmployeeTable.getFilter().equals("not-issued-date") && typeOfSortingEmployeeTable.getSearching().equals("")){
             listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssued();
        } else if(typeOfSortingEmployeeTable.getFilter().equals("not-issued-date") && !typeOfSortingEmployeeTable.getSearching().equals("")){
            listEmployee = (List<Employee>) employeeRepository.getNotFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(typeOfSortingEmployeeTable.getSearching());
        } else if(typeOfSortingEmployeeTable.getFilter().equals("issued-date") && typeOfSortingEmployeeTable.getSearching().equals("")) {
            listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssued();
        } else if (typeOfSortingEmployeeTable.getFilter().equals("issued-date") && !typeOfSortingEmployeeTable.getSearching().equals("")){
            listEmployee = (List<Employee>) employeeRepository.getFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(typeOfSortingEmployeeTable.getSearching());
        } else if(typeOfSortingEmployeeTable.getFilter().equals("end_date") && typeOfSortingEmployeeTable.getSearching().equals("")){
            listEmployee = (List<Employee>) employeeRepository.getEmployeesWithEndingDateWearForNextYear((Year.now().getValue()+1)+"_01_01",(Year.now().getValue()+1)+"_12_31");
        } else if(typeOfSortingEmployeeTable.getFilter().equals("end_date") && !typeOfSortingEmployeeTable.getSearching().equals("")){
            listEmployee = (List<Employee>) employeeRepository.getEmployeesWithEndingDateWearForNextYearByKeyword((Year.now().getValue()+1)+"_01_01",(Year.now().getValue()+1)+"_12_31",typeOfSortingEmployeeTable.getSearching());
        }
        for (Employee e: listEmployee) {
            listEmployeeForPrint.add(new EmployeeForPrint(e,issuedSIZRepository.getByEndWearDateForEmployee(e.getId()),employeeRepository.getPercentStaffingOfEmployee(e.getId(),e.getPost().getId())));
        }
        EmployeeStaffingExcelExporter excelExporter = new EmployeeStaffingExcelExporter(listEmployeeForPrint);
        excelExporter.export(response);
    }

    /**
     * Отображение таблицы с историей выдачи СИЗ выбранного сотрудника
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz/edit-staffing/employee/history-issued-siz/{id}")
    public String getHistoryInfoIssuedSizOfEmployee(@PathVariable(value = "id") long id, Model model) {
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findAllByEmployeeIdAndStatusOrderByDateIssued(id,"Списано");
        Employee employee = employeeRepository.findById(id).orElseThrow();
        model.addAttribute("employee",employee);
        model.addAttribute("vidanSIZ",issuedSIZS);
        return "user/mto/siz/issued/issued-siz-edit :: table-history-issued-siz";
    }



}
