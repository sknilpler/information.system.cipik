package com.information.system.cipik.repo;

import com.information.system.cipik.models.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee,Long> {
    Employee findByName(String name);
}
