package com.information.system.cipik.repo;

import com.information.system.cipik.models.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    /**
     * Поиск сотрудника по имени
     *
     * @param name имя сотрудника
     * @return сотрудник
     */
    Employee findByName(String name);

    /**
     * Найти всех сотрудников подразделения
     *
     * @param id ID подразделения
     * @return список сотрудников
     */
    List<Employee> findAllByKomplexId(Long id);

    /**
     * Найти всех сотрудников по ключевому слову
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность
     *
     * @param keyword ключевое слово
     * @return список сотрудников
     */
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

    /**
     * Найти всех сотрудников по ключевому слову
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность, подразделение
     *
     * @param keyword ключевое слово
     * @return список сотрудников
     */
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

    /**
     * Найти всех сотрудников по подразделению и по ключевому слову
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность
     *
     * @param keyword ключевое слово
     * @param id      ID подразделения
     * @return список сотрудников
     */
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
            "    ) LIKE %:keyword% AND e.post_id = p.id AND e.komplex_id =:id", nativeQuery = true)
    Iterable<Employee> findAllByPostAndKomplexIdAndKeyword(@Param("keyword") String keyword, @Param("id") Long id);

    /**
     * Поиск сотрудников по ключевому слову у которых выдан СИЗ
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность, подразделение
     *
     * @param keyword ключевое слово
     * @return список сотрудников отсортированный по дате окончания носки СИЗ
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate \n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\" \n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "INNER JOIN komplex k ON employee.komplex_id = k.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name,\n" +
            "     k.short_name\n" +
            "     ) LIKE %:keyword% \n" +
            "ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> findAllByPostAndKomplexAndKeywordOrderByEndDateIssued(@Param("keyword") String keyword);

    /**
     * Поиск сотрудников по ключевому слову и подразделению у которых выдан СИЗ
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность
     *
     * @param keyword ключевое слово
     * @param id      ID подразделения
     * @return список сотрудников отсортированный по дате окончания носки СИЗ
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate \n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\" \n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name\n" +
            "     ) LIKE %:keyword% AND employee.komplex_id =:id\n" +
            "ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> findAllByPostAndKomplexAndKeywordOrderByEndDateIssuedAndKomplex(@Param("keyword") String keyword,
                                                                                       @Param("id") Long id);

    /**
     * Получение % укомплектованности сотрудника относительно нормы выдачи
     *
     * @param id_empl ID сотрудника
     * @param id_post ID должности сотрудника
     * @return число в виде строки (% укомплектованности)
     */
    @Query(value = "SELECT COALESCE(TRUNCATE(((SELECT COUNT(siz_id) AS number FROM issuedsiz WHERE employee_id = :id_empl AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "(SELECT SUM(ipmstandard.issuance_rate) AS number FROM ipmstandard\n" +
            " WHERE post_id = :id_post)*100),0))", nativeQuery = true)
    String getPercentStaffingOfEmployee(@Param("id_empl") Long id_empl,
                                        @Param("id_post") Long id_post);

    /**
     * Получить список сотрудников у которых % укомплектованности СИЗ относительно нормы равен 100%
     * Т.е. у кого выдан весь необходимый СИЗ
     *
     * @return список сотрудников
     */
    @Query(value = "SELECT e.* FROM employee e\n" +
            " WHERE (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id AND issuedsiz.status LIKE \"Выдано\")/\n" +
            " (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard \n" +
            " WHERE ipmstandard.post_id = e.post_id)*100),0)=100) \n" +
            "order by e.komplex_id, e.post_id, e.surname", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployee();

    /**
     * Получить список сотрудников выбранного подразделения у которых % укомплектованности СИЗ относительно нормы равен 100%
     * Т.е. у кого выдан весь необходимый СИЗ
     *
     * @param id ID подразделения
     * @return список сотрудников
     */
    @Query(value = "SELECT e.* FROM employee e\n" +
            " WHERE (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id AND issuedsiz.status LIKE \"Выдано\")/\n" +
            " (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard \n" +
            " WHERE ipmstandard.post_id = e.post_id)*100),0)=100) AND e.komplex_id = :id\n" +
            "order by e.komplex_id, e.post_id, e.surname", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployeeByKomplex(@Param("id") Long id);

    /**
     * Получить список неукомплектованных сотрудников
     *
     * @return список сотрудников
     */
    @Query(value = "SELECT e.* FROM employee e WHERE (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "        (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)>=0) AND (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)<100) \n" +
            "order by e.komplex_id, e.post_id, e.surname", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployee();

    /**
     * Получить список неукомплектованных сотрудников по выбранному подразделению
     *
     * @param id ID подразделения
     * @return список сотрудников
     */
    @Query(value = "SELECT e.* FROM employee e WHERE (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "        (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)>=0) AND (\n" +
            "        SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE issuedsiz.employee_id = e.id AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "        (SELECT COUNT(ipmstandard.individual_protection_means_id) FROM ipmstandard \n" +
            "         WHERE ipmstandard.post_id = e.post_id)*100),0)<100) \n" +
            "AND e.komplex_id = :id\n" +
            "order by e.post_id, e.surname", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployeeByKomlex(@Param("id") Long id);

    /**
     * Получить список сотрудников которым выдан СИЗ
     *
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate \n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\" \n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getStaffingOfEmployeeOrderByEndDateIssued();

    /**
     * Получить список сотрудников выбранного подразделения которым выдан СИЗ
     *
     * @param id ID подразделения
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate \n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\" \n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "WHERE employee.komplex_id=:id\n" +
            "ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getStaffingOfEmployeeOrderByEndDateIssuedByKomplex(@Param("id") Long id);

    /**
     * Получить список сотрудников у которых срок носки СИЗ заканчивается в следующем году
     * Временной диапазон может быть любой
     *
     * @param date1 дата начала временного диапазона
     * @param date2 дата окончания временного диапазона
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "            FROM employee\n" +
            "            JOIN (SELECT e.id, MIN(i.date_end_wear) mindate\n" +
            "                 FROM employee e\n" +
            "                 JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "                 WHERE i.date_end_wear BETWEEN :date1 AND :date2\n" +
            "                 GROUP BY e.id) ee USING (id)\n" +
            "            ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getEmployeesWithEndingDateWearForNextYear(@Param("date1") String date1,
                                                                 @Param("date2") String date2);

    /**
     * Получить список сотрудников выбранного подразделения у которых срок носки СИЗ заканчивается в следующем году
     * Временной диапазон может быть любой
     *
     * @param date1 дата начала временного диапазона
     * @param date2 дата окончания временного диапазона
     * @param id    ID подразделения
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "            FROM employee\n" +
            "            JOIN (SELECT e.id, MIN(i.date_end_wear) mindate\n" +
            "                 FROM employee e\n" +
            "                 JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "                 WHERE i.date_end_wear BETWEEN :date1 AND :date2\n" +
            "                 GROUP BY e.id) ee USING (id)\n" +
            "            WHERE employee.komplex_id =:id\n" +
            "       ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getEmployeesWithEndingDateWearForNextYearByKomplex(@Param("date1") String date1,
                                                                          @Param("date2") String date2,
                                                                          @Param("id") Long id);

    /**
     * Получить список сотрудников по ключевому слову у которых срок носки СИЗ заканчивается в следующем году
     * Временной диапазон может быть любой
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность, подразделение
     *
     * @param date1   дата начала временного диапазона
     * @param date2   дата окончания временного диапазона
     * @param keyword ключевое слово
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "            FROM employee\n" +
            "            JOIN (SELECT e.id, MIN(i.date_end_wear) mindate\n" +
            "                 FROM employee e\n" +
            "                 JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "                 WHERE i.date_end_wear BETWEEN :date1 AND :date2\n" +
            "                 GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "INNER JOIN komplex k ON employee.komplex_id = k.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name,\n" +
            "     k.short_name\n" +
            "     ) LIKE %:keyword% " +
            "            ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getEmployeesWithEndingDateWearForNextYearByKeyword(@Param("date1") String date1,
                                                                          @Param("date2") String date2,
                                                                          @Param("keyword") String keyword);

    /**
     * Получить список сотрудников выбранного подразделения по ключевому слову у которых срок носки СИЗ заканчивается
     * в следующем году
     * Временной диапазон может быть любой
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность, подразделение
     *
     * @param date1   дата начала временного диапазона
     * @param date2   дата окончания временного диапазона
     * @param keyword ключевое слово
     * @param id      ID подразделения
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "            FROM employee\n" +
            "            JOIN (SELECT e.id, MIN(i.date_end_wear) mindate\n" +
            "                 FROM employee e\n" +
            "                 JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "                 WHERE i.date_end_wear BETWEEN :date1 AND :date2\n" +
            "                 GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name\n" +
            "     ) LIKE %:keyword% AND employee.komplex_id =:id" +
            "            ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getEmployeesWithEndingDateWearForNextYearByKeywordAndKomplex(@Param("date1") String date1,
                                                                                    @Param("date2") String date2,
                                                                                    @Param("keyword") String keyword,
                                                                                    @Param("id") Long id);

    /**
     * Получить список сотрудников которым по нормам выдан весь СИЗ
     *
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "      (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                               issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "            (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "            WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE and i.status LIKE \"Выдано\"\n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "WHERE ee.rate=100\n" +
            "ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployeeOrderByEndDateIssued();

    /**
     * Получить список сотрудников выбранного подразделения которым по нормам выдан весь СИЗ
     *
     * @param id ID подразделения
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "      (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                               issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "            (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "            WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE and i.status LIKE \"Выдано\"\n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "WHERE ee.rate=100\n" +
            "ORDER BY ee.mindate\n" +
            "WHERE employee.komplex_id=:id", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(@Param("id") Long id);

    /**
     * Получить список сотрудников которым недодали СИЗ
     *
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "      (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                               issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "            (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "            WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\"\n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "WHERE ee.rate>=0 AND ee.rate<100\n" +
            "ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployeeOrderByEndDateIssued();

    /**
     * Получить список сотрудников выбранного подразделения которым недодали СИЗ
     *
     * @param id ID подразделения
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "      (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                               issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "            (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "            WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\"\n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "WHERE ee.rate>=0 AND ee.rate<100\n" +
            "ORDER BY ee.mindate\n" +
            "WHERE employee.komplex_id=:id", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployeeOrderByEndDateIssuedAndKomplex(@Param("id") Long id);

    /**
     * Поиск сотрудников которым выдан весь СИЗ по ключевому слову
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность, подразделение
     *
     * @param keyword ключевое слово
     * @return список сотрудников отсортированный по подразделению, должности и фамилии
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "     (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                              issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "           (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "           WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "    FROM employee e\n" +
            "    JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "    WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\"\n" +
            "    GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "INNER JOIN komplex k ON employee.komplex_id = k.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name,\n" +
            "     k.short_name\n" +
            "     ) LIKE %:keyword% AND ee.rate=100\n" +
            "ORDER BY k.short_name, p.post_name, employee.surname", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployeeAndKeyword(@Param("keyword") String keyword);

    /**
     * Поиск сотрудников выбранного подразделения которым выдан весь СИЗ по ключевому слову
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность
     *
     * @param keyword ключевое слово
     * @param id      ID подразделения
     * @return список сотрудников отсортированный по должности и фамилии
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "     (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                              issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "           (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "           WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "    FROM employee e\n" +
            "    JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "    WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\"\n" +
            "    GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name\n" +
            "     ) LIKE %:keyword% AND ee.rate=100 AND employee.komplex_id =:id\n" +
            "ORDER BY p.post_name, employee.surname", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployeeAndKeywordAndKomplex(@Param("keyword") String keyword,
                                                                     @Param("id") Long id);

    /**
     * Поиск сотрудников которым не выдан весь СИЗ по ключевому слову
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность, подразделение
     *
     * @param keyword ключевое слово
     * @return список сотрудников отсортированный по подразделению, должности и фамилии
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "     (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                              issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "           (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "           WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "    FROM employee e\n" +
            "    JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "    WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\"\n" +
            "    GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "INNER JOIN komplex k ON employee.komplex_id = k.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name,\n" +
            "     k.short_name\n" +
            "     ) LIKE %:keyword% AND ee.rate>=0 AND ee.rate<100\n" +
            "ORDER BY k.short_name, p.post_name, employee.surname", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployeeAndKeyword(@Param("keyword") String keyword);

    /**
     * Поиск сотрудников выбранного подразделения которым не выдан весь СИЗ по ключевому слову
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность
     *
     * @param keyword ключевое слово
     * @param id      ID подразделения
     * @return список сотрудников отсортированный по должности и фамилии
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "     (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                              issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "           (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "           WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "    FROM employee e\n" +
            "    JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "    WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\"\n" +
            "    GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name\n" +
            "     ) LIKE %:keyword% AND ee.rate>=0 AND ee.rate<100 AND employee.komplex_id =:id\n" +
            "ORDER BY p.post_name, employee.surname", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployeeAndKeywordAndKomplex(@Param("keyword") String keyword,
                                                                        @Param("id") Long id);

    /**
     * Поиск сотрудников по ключевому слову которым выдан весь СИЗ
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность, подразделение
     *
     * @param keyword ключевое слово
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "      (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                               issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "            (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "            WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\" \n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "INNER JOIN komplex k ON employee.komplex_id = k.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name,\n" +
            "     k.short_name\n" +
            "     ) LIKE %:keyword% AND ee.rate=100\n" +
            "ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(@Param("keyword") String keyword);

    /**
     * Поиск сотрудников выбранного подразделения по ключевому слову которым выдан весь СИЗ
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность, подразделение
     *
     * @param keyword ключевое слово
     * @param id      ID подразделения
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "      (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                               issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "            (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "            WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\" \n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name\n" +
            "     ) LIKE %:keyword% AND ee.rate=100 AND employee.komplex_id =:id\n" +
            "ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getFullStaffingOfEmployeeAndKeywordOrderByEndDateIssuedAndKomplex(@Param("keyword") String keyword,
                                                                                         @Param("id") Long id);

    /**
     * Поиск сотрудников по ключевому слову которым не выдан весь СИЗ
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность, подразделение
     *
     * @param keyword ключевое слово
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "      (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                               issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "            (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "            WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\" \n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "INNER JOIN komplex k ON employee.komplex_id = k.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name,\n" +
            "     k.short_name\n" +
            "     ) LIKE %:keyword% AND ee.rate>=0 AND ee.rate<100\n" +
            "ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployeeAndKeywordOrderByEndDateIssued(@Param("keyword") String keyword);

    /**
     * Поиск сотрудников выбранного подразделения по ключевому слову которым не выдан весь СИЗ
     * Ключевое слово ищет по следующим полям: имя, фамилия, отчество, должность, подразделение
     *
     * @param keyword ключевое слово
     * @param id      ID подразделения
     * @return отсортированный по дате окончания носки список сотрудников
     */
    @Query(value = "SELECT employee.*\n" +
            "FROM employee\n" +
            "JOIN (SELECT e.id, MIN(i.date_end_wear) mindate, \n" +
            "      (SELECT TRUNCATE(((SELECT COUNT(issuedsiz.siz_id) FROM issuedsiz WHERE \n" +
            "                               issuedsiz.employee_id = e.id AND issuedsiz.date_end_wear > CURRENT_DATE AND issuedsiz.status LIKE \"Выдано\")/\n" +
            "            (SELECT SUM(ipmstandard.issuance_rate) FROM ipmstandard\n" +
            "            WHERE ipmstandard.post_id = e.post_id)*100),0)) AS rate\n" +
            "     FROM employee e\n" +
            "     JOIN issuedsiz i ON i.employee_id = e.id\n" +
            "     WHERE i.date_end_wear > CURRENT_DATE AND i.status LIKE \"Выдано\" \n" +
            "     GROUP BY e.id) ee USING (id)\n" +
            "INNER JOIN post p ON employee.post_id = p.id\n" +
            "WHERE CONCAT(\n" +
            "     employee.name,\n" +
            "     employee.surname,\n" +
            "     employee.patronymic,\n" +
            "     p.post_name\n" +
            "     ) LIKE %:keyword% AND ee.rate>=0 AND ee.rate<100 AND employee.komplex_id=:id\n" +
            "ORDER BY ee.mindate", nativeQuery = true)
    Iterable<Employee> getNotFullStaffingOfEmployeeAndKeywordOrderByEndDateIssuedAndKomplex(@Param("keyword") String keyword,
                                                                                            @Param("id") Long id);

    /**
     * Получить список сотрудников по должности
     *
     * @param id ID должности
     * @return список сотрудников
     */
    List<Employee> findAllByPostId(Long id);

    /**
     * Получить список сотрудников выбранного подразделения указанной должности
     *
     * @param komplex_id ID подразделения
     * @param post_id    ID должности
     * @return список сотрудников
     */
    List<Employee> findAllByKomplexIdAndPostId(Long komplex_id, Long post_id);
}
