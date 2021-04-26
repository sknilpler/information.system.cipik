package com.information.system.cipik.services;

import com.information.system.cipik.models.Employee;
import com.information.system.cipik.repo.EmployeeRepository;
import com.information.system.cipik.utils.csv.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CSVService {

    @Autowired
    EmployeeRepository employeeRepository;

    public void saveEmployee(MultipartFile file) {
        try {
            Iterable<Employee> employees = CSVHelper.csvToEmployees(file.getInputStream());
            employeeRepository.saveAll(employees);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

}
