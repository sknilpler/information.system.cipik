package com.information.system.cipik.controllers;

import com.information.system.cipik.models.Employee;
import com.information.system.cipik.repo.EmployeeRepository;
import com.information.system.cipik.repo.SIZRepository;
import com.information.system.cipik.utils.IPMByPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JSControler {

    @Autowired
    SIZRepository sizRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    ///////////////////SIZ////////////////////

    @GetMapping("/userPage/issued-siz/getEmployeeForOtdel/js/{id}")
    public List<Employee> getEmployeeForOtdel(@PathVariable Long id) {
        List<Employee> list = employeeRepository.findAllByOtdelId(id);
        System.out.println(list.size());
        list.forEach(employee -> {
            System.out.println(employee.toString());
        });
        return list;
    }

    @GetMapping("/userPage/issued-siz/getSizForPost/js/{id}")
    public Iterable<IPMByPost> getSIZForPost(@PathVariable Long id) {
        System.out.println(sizRepository.findAllByPostId(id));
        return sizRepository.findAllByPostId(id);
    }
}
