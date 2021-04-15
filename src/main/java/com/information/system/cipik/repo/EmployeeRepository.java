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
    Iterable<Employee> findAllByPostAndKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT\n" +
            "    e.*\n" +
            " FROM\n" +
            "    employee e,\n" +
            "    post p,\n" +
            "    otdel o\n" +
            " WHERE\n" +
            "    CONCAT(\n" +
            "        e.name,\n" +
            "        e.surname,\n" +
            "        e.patronymic,\n" +
            "        p.post_name,\n" +
            "        o.name\n" +
            "    ) LIKE %:keyword% AND e.post_id = p.id AND e.otdel_id = o.id", nativeQuery = true)
    Iterable<Employee> findAllByPostAndOtdelAndKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT TRUNCATE(((SELECT COUNT(siz_id) AS number FROM issuedsiz WHERE employee_id = :id_empl)/\n" +
            "(SELECT COUNT(individual_protection_means_id) AS number FROM ipmstandard\n" +
            " WHERE post_id = :id_post)*100),0)", nativeQuery = true)
    int getPercentStaffingOfEmployee(@Param("id_empl") Long id_empl, @Param("id_post") Long id_post);

    @Query(value = "SELECT e.* FROM employee e\n" +
            " WHERE (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            " (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            " WHERE ipmstandard.post_id = e.post_id)*100),0)=100)", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployee();

    @Query(value = "SELECT e.* FROM employee e WHERE (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)>=0) AND (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)<100)", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployee();


    @Query(value = "SELECT e.* FROM employee e,\n" +
            "             post p,\n" +
            "             otdel o\n" +
            " WHERE CONCAT(\n" +
            "            e.name,\n" +
            "            e.surname,\n" +
            "            e.patronymic,\n" +
            "            p.post_name,\n" +
            "            o.name\n" +
            "            ) LIKE %:keyword% AND e.post_id = p.id AND e.otdel_id = o.id AND " +
            " (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            " (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            " WHERE ipmstandard.post_id = e.post_id)*100),0)=100)", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployeeAndKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT e.* FROM employee e,\n" +
            "             post p,\n" +
            "             otdel o\n" +
            " WHERE CONCAT(\n" +
            "            e.name,\n" +
            "            e.surname,\n" +
            "            e.patronymic,\n" +
            "            p.post_name,\n" +
            "            o.name\n" +
            "            ) LIKE %:keyword% AND e.post_id = p.id AND e.otdel_id = o.id AND " +
            "        (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)>=0) AND (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)<100)", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployeeAndKeyword(@Param("keyword") String keyword);


    List<Employee> findAllByPostId(Long id);

    List<Employee> findAllByOtdelIdAndPostId(Long otdel_id,Long post_id);
}
