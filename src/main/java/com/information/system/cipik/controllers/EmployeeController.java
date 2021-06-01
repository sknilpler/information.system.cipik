package com.information.system.cipik.controllers;

import com.information.system.cipik.models.Employee;
import com.information.system.cipik.models.Komplex;
import com.information.system.cipik.models.Post;
import com.information.system.cipik.models.Role;
import com.information.system.cipik.repo.EmployeeRepository;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.repo.PostRepository;
import com.information.system.cipik.repo.RoleRepository;
import com.information.system.cipik.utils.EmployeeExcelExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PostRepository postRepository;
//    @Autowired
//    OtdelRepository otdelRepository;
    @Autowired
    KomplexRepository komplexRepository;
    @Autowired
    RoleRepository roleRepository;

   // private Komplex userKomplex;

    @GetMapping("/userPage/employee/employees-all")
    public String allEmployee(Model model, Authentication authentication) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")){
            employees = employeeRepository.findAll();
        }else{  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            employees = employeeRepository.findAllByKomplexId(komplex.getId());
            model.addAttribute("komplex",komplex);
        }
        model.addAttribute("employees", employees);
        return "user/employee/employes-managment-page";
    }

    @PostMapping("/userPage/employee/{id}/remove")
    public String deleteEmployee(@PathVariable(value = "id") long id, Model model) {
        Employee employee = employeeRepository.findById(id).orElseThrow();
        employeeRepository.delete(employee);
        return "redirect:/userPage/employee/employees-all";
    }

    @GetMapping("/userPage/employee/employee-add")
    public String addEmployee(Model model) {
        model.addAttribute("newEmployee", new Employee());
        model.addAttribute("komplexes", komplexRepository.findAll());
        model.addAttribute("posts", postRepository.findAll());
//        model.addAttribute("otdels", otdelRepository.findAll());
        model.addAttribute("dataStart", new Date());
        return "user/employee/employee-add";
    }

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @PostMapping("/userPage/employee/employee-add")
    public String addEmployee(@ModelAttribute("newEmployee") Employee newEmployee, @ModelAttribute("dataStart") Date dataStart, Model model) {
        System.out.println("дата: "+dataStart);
            newEmployee.setDataStartWork(dataStart);
        employeeRepository.save(newEmployee);
        return "redirect:/userPage/employee/employees-all";
    }

    @GetMapping("/userPage/employee/{id}/edit")
    public String employeeEdit(@PathVariable(value = "id") long id, Model model) {
        if (!employeeRepository.existsById(id)) {
            return "redirect:/userPage/employee/employees-all";
        }
        Employee employee = employeeRepository.findById(id).orElseThrow();
        model.addAttribute("posts", postRepository.findAll());
//        model.addAttribute("otdels", otdelRepository.findAll());
        model.addAttribute("komplexes", komplexRepository.findAll());
        model.addAttribute("employee", employee);
        model.addAttribute("dataStart",employee.getDataStartWork());
        return "user/employee/employee-edit";
    }

    @PostMapping("/userPage/employee/{id}/edit")
    public String employeeUpdate(@PathVariable(value = "id") long id, @ModelAttribute("employee") Employee newEmployee, @ModelAttribute("dataStart") Date dataStart, Model model) {
        newEmployee.setId(id);
        newEmployee.setDataStartWork(dataStart);
        System.out.println(newEmployee.getDataStartWork());
        employeeRepository.save(newEmployee);
        return "redirect:/userPage/employee/employees-all";
    }

    @GetMapping("/userPage/posts/add")
    public String postsAll(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "user/employee/post/post-all";
    }

    @PostMapping("/userPage/posts/add")
    public String postAdd(@RequestParam String postName, Model model) {
        Post post = new Post(postName);
        postRepository.save(post);
        return "redirect:/userPage/posts/add";
    }

    @GetMapping("/userPage/posts/{id}/edit")
    public String postEdit(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/userPage/posts/add";
        }
        Post post = postRepository.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "user/employee/post/post-edit";
    }

    @PostMapping("/userPage/posts/{id}/edit")
    public String postUpdate(@PathVariable(value = "id") long id, @RequestParam String postName, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setPostName(postName);
        postRepository.save(post);
        return "redirect:/userPage/posts/add";
    }

    @PostMapping("/userPage/posts/{id}/remove")
    public String postDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.deleteById(id);
        return "redirect:/userPage/posts/add";
    }

    /**
     * Экспорт списка сотрудников в EXCEL
     * @param response
     * @throws IOException
     */
    @GetMapping("/userPage/employee/export/excel")
    public void exportEmployeeToExcel(HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("dd_MM_yyyy_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=spisok_sotrudnikov_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        List<Employee> listEmployee;
        //если пользователь СуперЮзер то отображаем все данные по всем подразделениям
        if (role.getName().equals("ROLE_USER")){
            listEmployee = (List<Employee>) employeeRepository.findAll();
        }else{  //иначе определяем подразделение пользователя и по нему выводим информацию
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            listEmployee = (List<Employee>) employeeRepository.findAllByKomplexId(komplex.getId());
        }

        EmployeeExcelExporter excelExporter = new EmployeeExcelExporter(listEmployee);
        excelExporter.export(response);
    }
}
