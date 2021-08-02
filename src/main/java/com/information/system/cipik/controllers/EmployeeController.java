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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
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
    @Autowired
    KomplexRepository komplexRepository;
    @Autowired
    RoleRepository roleRepository;


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
        Employee employee = employeeRepository.findById(id).orElse(null);
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

    @PostMapping("/userPage/employee/employee-add")
    public String addEmployee(@ModelAttribute("newEmployee") Employee newEmployee, @RequestParam("dataStart") String dataStart, Model model) {
        Date dataStartWork = null;
        try {
            dataStartWork = new SimpleDateFormat("yyyy-MM-dd").parse(dataStart);
        } catch (ParseException e) {
            e.printStackTrace();
            dataStartWork = new Date();
        }
        newEmployee.setDataStartWork(dataStartWork);
        employeeRepository.save(newEmployee);
        return "redirect:/userPage/employee/employees-all";
    }

    @GetMapping("/userPage/employee/{id}/edit")
    public String employeeEdit(@PathVariable(value = "id") long id, Model model) {
        if (!employeeRepository.existsById(id)) {
            return "redirect:/userPage/employee/employees-all";
        }
        Employee employee = employeeRepository.findById(id).orElse(null);
        model.addAttribute("posts", postRepository.findAll());
//      model.addAttribute("otdels", otdelRepository.findAll());
        model.addAttribute("komplexes", komplexRepository.findAll());
        model.addAttribute("employee", employee);
        model.addAttribute("dataStart",employee.getDataStartWork());
        return "user/employee/employee-edit";
    }

    @PostMapping("/userPage/employee/{id}/edit")
    public String employeeUpdate(@PathVariable(value = "id") long id, @ModelAttribute("employee") Employee newEmployee, @RequestParam("dataStart") String dataStart, Model model) {
        Date dataStartWork = null;
        try {
            dataStartWork = new SimpleDateFormat("yyyy-MM-dd").parse(dataStart);
            newEmployee.setDataStartWork(dataStartWork);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newEmployee.setId(id);
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
        Post post = postRepository.findById(id).orElse(null);
        model.addAttribute("post", post);
        return "user/employee/post/post-edit";
    }

    @PostMapping("/userPage/posts/{id}/edit")
    public String postUpdate(@PathVariable(value = "id") long id, @RequestParam String postName, Model model) {
        Post post = postRepository.findById(id).orElse(null);
        post.setPostName(postName);
        postRepository.save(post);
        return "redirect:/userPage/posts/add";
    }

    @PostMapping("/userPage/posts/{id}/remove")
    public String postDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElse(null);
        postRepository.deleteById(id);
        return "redirect:/userPage/posts/add";
    }

    /**
     * Экспорт списка сотрудников в EXCEL
     * @param response
     * @throws IOException
     */
    @PostMapping("/userPage/employee/export/excel/{keyword}")
    public void exportEmployeeToExcel(@PathVariable(value = "keyword") String keyword,HttpServletResponse response, Authentication authentication) throws IOException {
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
        if (role.getName().equals("ROLE_USER")) {
            if (keyword.equals("0")) {
                listEmployee = (List<Employee>) employeeRepository.findAll();
            } else {
                listEmployee = (List<Employee>) employeeRepository.findAllByPostAndKomplexAndKeyword(keyword);
            }
        } else {
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (keyword.equals("0")) {
                listEmployee = employeeRepository.findAllByKomplexId(komplex.getId());
            } else {
                listEmployee = (List<Employee>) employeeRepository.findAllByPostAndKomplexIdAndKeyword(keyword, komplex.getId());
            }
        }

        EmployeeExcelExporter excelExporter = new EmployeeExcelExporter(listEmployee);
        excelExporter.export(response);
    }

    /**
     * Поиск и фильтрация списка сотрудников
     *
     * @param keyword ключевое слово для поиска и фильтрации
     * @param authentication параметры аутентификации
     * @param model модель аттрибутов страницы
     * @return список сотрудников
     */
    @GetMapping("/userPage/employee/search/{keyword}")
    public String searchEmployee(@PathVariable(value = "keyword") String keyword, Authentication authentication, Model model) {
        //определение текущей роли пользователя
        Role role = roleRepository.findByName(authentication.getAuthorities().stream().collect(toCollection(ArrayList::new)).get(0).getAuthority());
        Iterable<Employee> employees;
        if (role.getName().equals("ROLE_USER")) {
            if (keyword.equals("0")) {
                employees = employeeRepository.findAll();
            } else {
                employees = employeeRepository.findAllByPostAndKomplexAndKeyword(keyword);
            }
        } else {
            Komplex komplex = komplexRepository.findByRoleId(role.getId());
            if (keyword.equals("0")) {
                employees = employeeRepository.findAllByKomplexId(komplex.getId());
            } else {
                employees = employeeRepository.findAllByPostAndKomplexIdAndKeyword(keyword, komplex.getId());
            }
        }
        model.addAttribute("employees", employees);
        return "user/employee/employes-managment-page :: bd-example";
    }
}
