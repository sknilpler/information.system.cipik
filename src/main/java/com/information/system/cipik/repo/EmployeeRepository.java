package com.information.system.cipik.repo;

import com.information.system.cipik.models.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee,Long> {
    Employee findByName(String name);
    List<Employee> findAllByOtdelId(Long id);

    @Query(value = "SELECT\n" +
            "    e.*\n" +
            " FROM\n" +
            "    employee e,\n" +
            "    post p\n" +
            " WHERE\n" +
            "    CONCAT(\n" +
            "        e.name,\n" +
            "        e.surname,\n" +
            "        e.patronymic,\n" +
            "        p.post_name\n" +
            "    ) LIKE %:keyword% AND e.post_id = p.id", nativeQuery = true)
    Iterable<Employee> findAllByKeword(@Param("keyword") String keyword);

    List<Employee> findAllByPostId(Long id);

    List<Employee> findAllByOtdelIdAndPostId(Long otdel_id,Long post_id);
}
