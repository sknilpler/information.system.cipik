package com.information.system.cipik.controllers;

import com.information.system.cipik.models.*;
import com.information.system.cipik.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class SIZController {

    @Autowired
    SIZRepository sizRepository;
    @Autowired
    IPMStandardRepository ipmStandardRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    OtdelRepository otdelRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    IssuedSIZRepository issuedSIZRepository;
    @Autowired
    ItemsRepository itemsRepository;

    private Iterable<Post> listPosts;
    private Post postAddToNorm;
    private Post postToIssued;
    private Employee employeeForIssuedSIZ;
    private String filerIssuedSizAll;
    private boolean sortedByEndIssuedDate;

    @GetMapping("/userPage/siz-types")
    public String allSIZ(Model model) {
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("ipms", individualProtectionMeans);
        return "user/mto/siz/types-of-siz";
    }


    @PostMapping("/userPage/siz-types")
    public String addSIZ(@RequestParam String nameSIZ, @RequestParam String ed_izm, Model model) {
        IndividualProtectionMeans individualProtectionMeans = new IndividualProtectionMeans(nameSIZ, ed_izm);
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
    public String updateSIZ(@PathVariable(value = "id") long id, @RequestParam String nameSIZ, @RequestParam String ed_izm, Model model) {
        IndividualProtectionMeans individualProtectionMeans = sizRepository.findById(id).orElseThrow();
        individualProtectionMeans.setNameSIZ(nameSIZ);
        individualProtectionMeans.setEd_izm(ed_izm);
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
                              @RequestParam int serviceLife, @RequestParam String regulatoryDocuments,@RequestParam String typeIPM, Model model) {
        if (!postAddToNorm.getPostName().equals("")) {
            IndividualProtectionMeans siz = sizRepository.findById(dropSIZ).orElseThrow();
            IPMStandard norma = new IPMStandard(postAddToNorm, siz, issuanceRate,serviceLife,regulatoryDocuments,typeIPM);
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
    public String normsUpdate(@PathVariable(value = "id") long id, @RequestParam Rashodniki dropSIZ, @RequestParam int issuanceRate,
                              @RequestParam int serviceLife, @RequestParam String regulatoryDocuments, Model model) {
        System.out.println(dropSIZ.getName());
        IPMStandard norma = ipmStandardRepository.findById(id).orElseThrow();
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
    public String notIssuedSIZAll(Model model) {
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findByStatus("На складе");
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("typeSIZS",individualProtectionMeans);
        model.addAttribute("notIssuedSIZ",issuedSIZS);
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
        if (height.equals("")) {
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
    public String notIssuedSIZDelete(@PathVariable(value = "list") List<Long> list, Model model){
        for (Long id : list) {
            issuedSIZRepository.deleteById(id);
        }
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findByStatus("На складе");
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
    public String issuedSIZAll(Model model, String keyword) {
        Post post = new Post("");
        Employee employee = new Employee("","","","","",null,null);
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("otdels", otdelRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("selectedEmployee",employee);
        model.addAttribute("selected", post);
        model.addAttribute("ipmStandardRepository", ipmStandardRepository);
        return "user/mto/siz/issued/issued-siz-add";
    }

    /**
     * Обновление таблицы сотрудников по выбранному отделу
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/getEmployeeForOtdel/{id}")
    public String getEmployeeForOtdel(@PathVariable(value = "id") long id, Model model) {
        List<Employee> employees = employeeRepository.findAllByOtdelId(id);
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
    public String getEmployeeForPost(@PathVariable(value = "id") long id, Model model) {
        List<Employee> employees = employeeRepository.findAllByPostId(id);
        model.addAttribute("employees", employees);
        return "user/mto/siz/issued/issued-siz-add :: table-employees";
    }

    /**
     * Обновление таблицы сотрудников по отделу и должности
     * @param id_otdel
     * @param id_post
     * @param model
     * @return
     */
    @GetMapping("/userPage/issued-siz/getEmployeeForOtdelAndPost/{id_otdel}/{id_post}")
    public String getEmployeeForOtdelAndPost(@PathVariable(value = "id_otdel") long id_otdel, @PathVariable(value = "id_post") long id_post, Model model) {
        List<Employee> employees = employeeRepository.findAllByOtdelIdAndPostId(id_otdel,id_post);
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
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findAllByEmployeeId(id);
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
        String typeSIZ = ipmStandard.getTypeIPM();

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
                if ((issuedSIZS != null) && (issuedSIZS.size() > 0)) {
                    if (issuedSIZS.size() < number) {
                        message = "Для " + employee.getSurname() + " " + employee.getName() + " СИЗ на складе не достаточно, выдано " + issuedSIZS.size() + " из " + number + " запрошенных";
                        number = issuedSIZS.size();
                    }
                    for (int i = 0; i < number; i++) {
                        IssuedSIZ siz = issuedSIZS.get(i);

                        siz.setEmployee(employee);
                        siz.setDateIssued(dateIssued);
                        siz.setDateEndWear(dateEndWear);
                        siz.setStatus("Выдано");
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

        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeId(employeeForIssuedSIZ.getId());
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
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeId(employeeForIssuedSIZ.getId());
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
        issuedSIZ.setDateIssued(null);
        issuedSIZ.setDateEndWear(null);
        issuedSIZ.setEmployee(null);
        issuedSIZ.setStatus("На складе");
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeId(employeeForIssuedSIZ.getId());
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
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeId(employeeForIssuedSIZ.getId());
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
    public String searchEmployee(@PathVariable(value = "keyword") String keyword, Model model) {
        Iterable<Employee> employees;
        if (keyword.equals("0")){
            employees = employeeRepository.findAll();
        }else{
            employees = employeeRepository.findAllByPostAndKeyword(keyword);
        }
        model.addAttribute("employees", employees);
        return "user/mto/siz/issued/issued-siz-add :: table-employees";
    }

    /**
     * Поиск СИЗ на складе
     * @param keyword
     * @param model
     * @return
     */
    @GetMapping("/userPage/not-issued-siz/search/stock-siz/{keyword}")
    public String searchStockSiz(@PathVariable(value = "keyword") String keyword, Model model) {
        List<IssuedSIZ> issuedSIZS;
        if (keyword.equals("0")) {
            issuedSIZS = issuedSIZRepository.findByStatus("На складе");
        } else {
            issuedSIZS = issuedSIZRepository.findByStatusAndSizeLikeOrHeightLikeOrSizNameSIZLike("На складе","%"+keyword+"%","%"+keyword+"%", "%"+keyword+"%");
        }
        Iterable<IndividualProtectionMeans> individualProtectionMeans = sizRepository.findAll();
        model.addAttribute("typeSIZS", individualProtectionMeans);
        model.addAttribute("notIssuedSIZ", issuedSIZS);
        return "user/mto/siz/siz-from-stock :: table-sock-siz";
    }

    //////////////////////Укомплектованность сотрудников СИЗ//////////////////////

    /**
     * Первоначальное открытие страницы укомплектованности СИЗ
     * @param model
     * @return
     */
    @GetMapping("/userPage/employee-siz")
    public String staffingOfAllEmployeesSIZ(Model model) {
        filerIssuedSizAll = "all";
        Iterable<Employee> employees = employeeRepository.findAll();
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
    public String filterStaffingOfAllEmployeesSIZ(@PathVariable(value = "keyword") String keyword, Model model) {
        filerIssuedSizAll = keyword;
        sortedByEndIssuedDate = false;
        Iterable<Employee> employees;
        if (keyword.equals("not-issued")){
            employees = employeeRepository.getNotFullStaffingOfEmployee();
        } else if (keyword.equals("issued")){
            employees = employeeRepository.getFullStaffingOfEmployee();
        } else {
            employees = employeeRepository.findAll();
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
    public String sortingByDateStaffingOfAllEmployeesSIZ(Model model) {
        sortedByEndIssuedDate = true;
        Iterable<Employee> employees;
        if (filerIssuedSizAll.equals("not-issued")){
            employees = employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssued();
        } else if (filerIssuedSizAll.equals("issued")){
            employees = employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssued();
        } else {
            employees = employeeRepository.findAll();
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
    public String searchStaffingOfAllEmployeesSIZ(@PathVariable(value = "keyword") String keyword, Model model) {
        Iterable<Employee> employees;
        if (keyword.equals("0")) {
            if (filerIssuedSizAll.equals("not-issued")) {
                if (sortedByEndIssuedDate)
                    employees = employeeRepository.getNotFullStaffingOfEmployeeOrderByEndDateIssued();
                else
                    employees = employeeRepository.getNotFullStaffingOfEmployee();
            } else if (filerIssuedSizAll.equals("issued")) {
                if (sortedByEndIssuedDate)
                    employees = employeeRepository.getFullStaffingOfEmployeeOrderByEndDateIssued();
                else
                    employees = employeeRepository.getFullStaffingOfEmployee();
            } else {
                employees = employeeRepository.findAll();
            }
        } else {
            if (filerIssuedSizAll.equals("not-issued")) {
                if (sortedByEndIssuedDate)
                    employees = employeeRepository.getNotFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(keyword);
                else
                    employees = employeeRepository.getNotFullStaffingOfEmployeeAndKeyword(keyword);
            } else if (filerIssuedSizAll.equals("issued")) {
                if (sortedByEndIssuedDate)
                    employees = employeeRepository.getFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(keyword);
                else
                    employees = employeeRepository.getFullStaffingOfEmployeeAndKeyword(keyword);
            } else {
                if (sortedByEndIssuedDate)
                    employees = employeeRepository.findAllByPostAndOtdelAndKeywordOrderByEndDateIssued(keyword);
                else
                    employees = employeeRepository.findAllByPostAndOtdelAndKeyword(keyword);
            }
        }
        model.addAttribute("employees", employees);
        model.addAttribute("employeeRepository",employeeRepository);
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
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findAllByEmployeeId(id);
        Employee employee = employeeRepository.findById(id).orElseThrow();
        model.addAttribute("employee",employee);
        model.addAttribute("vidanSIZ",issuedSIZS);
        return "user/mto/siz/issued/issued-siz-all :: table-issued-siz";
    }

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
        List<IssuedSIZ> issuedSIZS = issuedSIZRepository.findAllByEmployeeId(id);
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
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeId(employeeForIssuedSIZ.getId());
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
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeId(employeeForIssuedSIZ.getId());
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
        issuedSIZ.setEmployee(null);
        issuedSIZ.setWriteOffAct(actName);
        issuedSIZRepository.save(issuedSIZ);
        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeId(employeeForIssuedSIZ.getId());
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
        String typeSIZ = ipmStandard.getTypeIPM();

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
                if (issuedSIZS.size() < number) {
                    message = "Для " + employeeForIssuedSIZ.getSurname() + " " + employeeForIssuedSIZ.getName() + " СИЗ на складе не достаточно, выдано " + issuedSIZS.size() + " из " + number + " запрошенных";
                    number = issuedSIZS.size();
                }
                for (int i = 0; i < number; i++) {
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

        List<IssuedSIZ> issuedSIZS2 = issuedSIZRepository.findAllByEmployeeId(employeeForIssuedSIZ.getId());
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



}
