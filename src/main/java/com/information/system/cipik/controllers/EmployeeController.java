package com.information.system.cipik.controllers;

import com.information.system.cipik.models.Employee;
import com.information.system.cipik.repo.EmployeeRepository;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.repo.OtdelRepository;
import com.information.system.cipik.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    OtdelRepository otdelRepository;
    @Autowired
    KomplexRepository komplexRepository;

    @GetMapping("/userPage/employee/employees-all")
    public String allEmployee(Model model) {
        Iterable<Employee> employees = employeeRepository.findAll();
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
        model.addAttribute("otdels", otdelRepository.findAll());
        return "user/employee/employee-add";
    }

    @PostMapping("/userPage/employee/employee-add")
    public String addEmployee(@ModelAttribute("newEmployee") Employee newEmployee, Model model) {
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
        model.addAttribute("otdels", otdelRepository.findAll());
        model.addAttribute("employee", employee);
        return "user/employee/employee-edit";
    }

    @PostMapping("/userPage/employee/{id}/edit")
    public String employeeUpdate(@PathVariable(value = "id") long id, @ModelAttribute("employee") Employee newEmployee, Model model) {
        newEmployee.setId(id);
        System.out.println(newEmployee.getId());
        employeeRepository.save(newEmployee);
        return "redirect:/userPage/employee/employees-all";
    }
}
