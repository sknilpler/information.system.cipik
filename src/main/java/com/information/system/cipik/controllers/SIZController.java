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
    @PostMapping("/userPage/issued-siz/{id}/add")
    public String addIssuedSiz(@PathVariable(value = "id") long id, Model model) {

       // IssuedSIZ issuedSIZ = issuedSIZRepository.findById(id).orElseThrow();

        return "redirect:/userPage/siz/norms";
    }

}
