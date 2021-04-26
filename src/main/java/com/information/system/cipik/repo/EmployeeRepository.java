package com.information.system.cipik.repo;

import com.information.system.cipik.models.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee,Long> {
    Employee findByName(String name);
    List<Employee> findAllByKomplexId(Long id);

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
            "    komplex k\n" +
            " WHERE\n" +
            "    CONCAT(\n" +
            "        e.name,\n" +
            "        e.surname,\n" +
            "        e.patronymic,\n" +
            "        p.post_name,\n" +
            "        k.short_name\n" +
            "    ) LIKE %:keyword% AND e.post_id = p.id AND e.komplex_id = k.id", nativeQuery = true)
    Iterable<Employee> findAllByPostAndKomplexAndKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT\n" +
            "    e.*\n" +
            " FROM\n" +
            "    employee e,\n" +
            "    post p,\n" +
            "    komplex k,\n" +
            "    issuedsiz i\n" +
            " WHERE\n" +
            "    CONCAT(\n" +
            "        e.name,\n" +
            "        e.surname,\n" +
            "        e.patronymic,\n" +
            "        p.post_name,\n" +
            "        k.short_name\n" +
            "    ) LIKE %:keyword% AND e.post_id = p.id AND i.employee_id = e.id AND e.komplex_id = k.id", nativeQuery = true)
    Iterable<Employee> findAllByPostAndKomplexAndKeywordOrderByEndDateIssued(@Param("keyword") String keyword);

    @Query(value = "SELECT TRUNCATE(((SELECT COUNT(siz_id) AS number FROM issuedsiz WHERE employee_id = :id_empl)/\n" +
            "(SELECT SUM(ipmstandard.issuance_rate) AS number FROM ipmstandard\n" +
            " WHERE post_id = :id_post)*100),0)", nativeQuery = true)
    int getPercentStaffingOfEmployee(@Param("id_empl") Long id_empl, @Param("id_post") Long id_post);

    @Query(value = "SELECT e.* FROM employee e, post p, komplex k\n" +
            " WHERE e.post_id = p.id AND e.komplex_id = k.id AND (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            " (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard \n" +
            " WHERE ipmstandard.post_id = e.post_id)*100),0)=100) \n"+
            "order by k.short_name, p.post_name, e.surname", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployee();

    @Query(value = "SELECT e.* FROM employee e, post p, komplex k WHERE e.post_id = p.id AND e.komplex_id = k.id AND (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)>=0) AND (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)<100) \n"+
            "order by k.short_name, p.post_name, e.surname", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployee();

    @Query(value = "SELECT e.* FROM employee e, post p, komplex k, issuedsiz i\n" +
            " WHERE e.post_id = p.id AND e.komplex_id = k.id AND i.employee_id = e.id AND (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            " (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard \n" +
            " WHERE ipmstandard.post_id = e.post_id)*100),0)=100) \n" +
            "            order by abs(now() - i.date_end_wear) desc limit 1", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployeeOrderByEndDateIssued();

    @Query(value = "SELECT e.* FROM employee e, post p, komplex k, issuedsiz i WHERE e.post_id = p.id AND e.komplex_id = k.id AND i.employee_id = e.id AND (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)>=0) AND (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)<100) \n" +
            "            order by abs(now() - i.date_end_wear) desc limit 1", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployeeOrderByEndDateIssued();

    @Query(value = "SELECT e.* FROM employee e,\n" +
            "             post p,\n" +
            "             komplex k\n" +
            " WHERE CONCAT(\n" +
            "            e.name,\n" +
            "            e.surname,\n" +
            "            e.patronymic,\n" +
            "            p.post_name,\n" +
            "            k.short_name\n" +
            "            ) LIKE %:keyword% AND e.post_id = p.id AND e.komplex_id = k.id AND " +
            " (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            " (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            " WHERE ipmstandard.post_id = e.post_id)*100),0)=100) \n" +
            "order by k.short_name, p.post_name, e.surname", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployeeAndKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT e.* FROM employee e,\n" +
            "             post p,\n" +
            "             komplex k\n" +
            " WHERE CONCAT(\n" +
            "            e.name,\n" +
            "            e.surname,\n" +
            "            e.patronymic,\n" +
            "            p.post_name,\n" +
            "            k.short_name\n" +
            "            ) LIKE %:keyword% AND e.post_id = p.id AND e.komplex_id = k.id AND " +
            "        (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)>=0) AND (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)<100) \n"+
            "order by k.short_name, p.post_name, e.surname", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployeeAndKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT e.* FROM employee e,\n" +
            "                        post p,\n" +
            "                        komplex k,\n" +
            "                        issuedsiz i\n" +
            "            WHERE CONCAT(\n" +
            "                       e.name,\n" +
            "                       e.surname,\n" +
            "                       e.patronymic,\n" +
            "                       p.post_name,\n" +
            "                       k.short_name\n" +
            "                       ) LIKE %:keyword% AND e.post_id = p.id AND e.komplex_id = k.id AND \n" +
            "                        i.employee_id = e.id AND\n" +
            "            (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "            (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "            WHERE ipmstandard.post_id = e.post_id)*100),0)=100)\n" +
            "            order by abs(now() - i.date_end_wear) desc limit 1",nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(@Param("keyword") String keyword);

    @Query(value = "SELECT e.* FROM employee e,\n" +
            "             post p,\n" +
            "             komplex k,\n" +
            "             issuedsiz i\n" +
            " WHERE CONCAT(\n" +
            "            e.name,\n" +
            "            e.surname,\n" +
            "            e.patronymic,\n" +
            "            p.post_name,\n" +
            "            k.short_name\n" +
            "            ) LIKE %:keyword% AND e.post_id = p.id AND e.komplex_id = k.id AND " +
            "            i.employee_id = e.id AND\n" +
            "        (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)>=0) AND (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id)/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)<100) \n" +
            "         order by abs(now() - i.date_end_wear) desc limit 1", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(@Param("keyword") String keyword);

    List<Employee> findAllByPostId(Long id);

    List<Employee> findAllByKomplexIdAndPostId(Long komplex_id,Long post_id);
}
