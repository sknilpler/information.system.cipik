package com.information.system.cipik.repo;

import com.information.system.cipik.models.IssuedSIZ;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssuedSIZRepository extends CrudRepository<IssuedSIZ, Long> {

    List<IssuedSIZ> findAllByEmployeeId(Long id);

    /**
     * <b>Поиск СИЗ по выбранному статусу</b>
     *
     * @param status статус СИЗ
     * @return список СИЗ
     */
    List<IssuedSIZ> findByStatus(String status);

    Iterable<IssuedSIZ> findByStatusAndKomplexId(String status, Long komplex_id);

    Iterable<IssuedSIZ> findByEmployeeIdNotNullAndStatusLike(String status);

    Iterable<IssuedSIZ> findByEmployeeIdNotNullAndStatusLikeOrderByDateEndWear(String status);

    Iterable<IssuedSIZ> findByEmployeeIdNotNullAndStatusLikeOrderByEmployeeSurname(String status);

    Iterable<IssuedSIZ> findByEmployeeIdNotNullAndStatusLikeOrderBySizTypeIPM(String status);

    Iterable<IssuedSIZ> findByEmployeeIdNotNullAndStatusLikeOrderByEmployeeKomplexShortNameAscEmployeePostPostNameAsc(String status);

    /**
     * <b>Фильтрация СИЗ по подразделениям и статусу</b>
     *
     * @param status     Статус СИЗ
     * @param komplex_id ID подразделения
     * @return список СИЗ
     */
    @Query(value = "SELECT\n" +
            "    issuedsiz.*\n" +
            "FROM\n" +
            "    issuedsiz\n" +
            "LEFT JOIN employee ON issuedsiz.employee_id = employee.id\n" +
            "LEFT JOIN komplex ON employee.otdel_id = komplex.id\n" +
            "WHERE\n" +
            "    issuedsiz.status = :status AND employee.komplex_id = :id\n", nativeQuery = true)
    Iterable<IssuedSIZ> findByStatusAndEmployeeKomplexId(@Param("status") String status,
                                                         @Param("id") Long komplex_id);

    /**
     * <b>Поиск выданного СИЗ по ключевому слову и подразделению</b>
     *
     * @param status     Статус СИЗ
     * @param komplex_id ID подразделения
     * @param keyword ключевое слово
     * @return список СИЗ
     */
    @Query(value = "SELECT\n" +
            "    issuedsiz.*\n" +
            "FROM\n" +
            "    issuedsiz\n" +
            "LEFT JOIN individual_protection_means ON issuedsiz.siz_id = individual_protection_means.id\n" +
            "LEFT JOIN employee ON issuedsiz.employee_id = employee.id\n" +
            "LEFT JOIN post ON employee.post_id = post.id\n" +
            "LEFT JOIN komplex ON employee.otdel_id = komplex.id\n" +
            "WHERE\n" +
            "    issuedsiz.status = :status AND employee.komplex_id = :id AND \n" +
            "    (individual_protection_means.namesiz LIKE %:keyword% OR\n" +
            "    individual_protection_means.typeipm LIKE %:keyword% OR\n" +
            "    issuedsiz.nomenclature_number LIKE %:keyword% OR\n" +
            "    employee.surname LIKE %:keyword% OR\n" +
            "    post.post_name LIKE %:keyword% OR\n" +
            "    komplex.short_name LIKE %:keyword%)", nativeQuery = true)
    Iterable<IssuedSIZ> findByStatusAndEmployeeKomplexIdAndKeyword(@Param("status") String status,
                                                                   @Param("id") Long komplex_id,
                                                                   @Param("keyword") String keyword);

    /**
     * <b>Поиск выданного СИЗ по ключевому слову</b>
     *
     * @param status     Статус СИЗ
     * @param keyword ключевое слово
     * @return список СИЗ
     */
    @Query(value = "SELECT\n" +
            "    issuedsiz.*\n" +
            "FROM\n" +
            "    issuedsiz\n" +
            "LEFT JOIN individual_protection_means ON issuedsiz.siz_id = individual_protection_means.id\n" +
            "LEFT JOIN employee ON issuedsiz.employee_id = employee.id\n" +
            "LEFT JOIN post ON employee.post_id = post.id\n" +
            "LEFT JOIN komplex ON employee.otdel_id = komplex.id\n" +
            "WHERE\n" +
            "    issuedsiz.status = :status AND \n" +
            "    (individual_protection_means.namesiz LIKE %:keyword% OR\n" +
            "    individual_protection_means.typeipm LIKE %:keyword% OR\n" +
            "    employee.surname LIKE %:keyword% OR\n" +
            "    issuedsiz.nomenclature_number LIKE %:keyword% OR\n" +
            "    post.post_name LIKE %:keyword% OR\n" +
            "    komplex.short_name LIKE %:keyword%)", nativeQuery = true)
    Iterable<IssuedSIZ> findByStatusAndKeyword(@Param("status") String status, @Param("keyword") String keyword);

    /**
     * <b>Фильтрация СИЗ по подразделениям и статусу и сортировка по дате окончания носки</b>
     *
     * @param status     Статус СИЗ
     * @param komplex_id ID подразделения
     * @return список СИЗ
     */
    @Query(value = "SELECT\n" +
            "    issuedsiz.*\n" +
            "FROM\n" +
            "    issuedsiz\n" +
            "LEFT JOIN employee ON issuedsiz.employee_id = employee.id\n" +
            "LEFT JOIN komplex ON employee.otdel_id = komplex.id\n" +
            "WHERE\n" +
            "    issuedsiz.status = :status AND employee.komplex_id = :id\n" +
            "ORDER BY issuedsiz.date_end_wear", nativeQuery = true)
    Iterable<IssuedSIZ> findByStatusAndEmployeeKomplexIdAndSortingByDate(@Param("status") String status,
                                                                         @Param("id") Long komplex_id);

    /**
     * <b>Фильтрация СИЗ по подразделениям и статусу и сортировка по ФИО сотрудника</b>
     *
     * @param status     Статус СИЗ
     * @param komplex_id ID подразделения
     * @return список СИЗ
     */
    @Query(value = "SELECT\n" +
            "    issuedsiz.*\n" +
            "FROM\n" +
            "    issuedsiz\n" +
            "LEFT JOIN employee ON issuedsiz.employee_id = employee.id\n" +
            "LEFT JOIN komplex ON employee.otdel_id = komplex.id\n" +
            "WHERE\n" +
            "    issuedsiz.status = :status AND employee.komplex_id = :id\n" +
            "ORDER BY employee.surname", nativeQuery = true)
    Iterable<IssuedSIZ> findByStatusAndEmployeeKomplexIdAndSortingByFIO(@Param("status") String status,
                                                                         @Param("id") Long komplex_id);

    /**
     * <b>Фильтрация СИЗ по подразделениям и статусу и сортировка по типу СИЗ</b>
     *
     * @param status     Статус СИЗ
     * @param komplex_id ID подразделения
     * @return список СИЗ
     */
    @Query(value = "SELECT\n" +
            "    issuedsiz.*\n" +
            "FROM\n" +
            "    issuedsiz\n" +
            "LEFT JOIN employee ON issuedsiz.employee_id = employee.id\n" +
            "LEFT JOIN komplex ON employee.otdel_id = komplex.id\n" +
            "LEFT JOIN individual_protection_means ON issuedsiz.siz_id = individual_protection_means.id\n" +
            "WHERE\n" +
            "    issuedsiz.status = :status AND employee.komplex_id = :id\n" +
            "ORDER BY individual_protection_means.typeipm", nativeQuery = true)
    Iterable<IssuedSIZ> findByStatusAndEmployeeKomplexIdAndSortingByType(@Param("status") String status,
                                                                         @Param("id") Long komplex_id);

    /**
     * <b>Фильтрация СИЗ по подразделениям и статусу и сортировка по комплексу и должности</b>
     *
     * @param status     Статус СИЗ
     * @param komplex_id ID подразделения
     * @return список СИЗ
     */
    @Query(value = "SELECT\n" +
            "    issuedsiz.*\n" +
            "FROM\n" +
            "    issuedsiz\n" +
            "LEFT JOIN employee ON issuedsiz.employee_id = employee.id\n" +
            "LEFT JOIN komplex ON employee.otdel_id = komplex.id\n" +
            "LEFT JOIN post ON employee.post_id = post.id\n" +
            "WHERE\n" +
            "    issuedsiz.status = :status AND employee.komplex_id = :id\n" +
            "ORDER BY\n" +
            "    komplex.short_name, post.post_name", nativeQuery = true)
    Iterable<IssuedSIZ> findByStatusAndEmployeeKomplexIdAndSortingByKomplexAndPost(@Param("status") String status,
                                                                        @Param("id") Long komplex_id);

    List<IssuedSIZ> findByStatusAndSizeLikeOrHeightLikeOrSizNameSIZLike(String status, String size, String height, String nameSIZ);


    /**
     * <b>Получить сгруппированный список не выданного СИЗ на складе предприятия</b>
     *
     * @return сгруппированный по типу, размеру и росту список СИЗ на складе
     */
    @Query(value = "SELECT s.id,i.nomenclature_number,s.namesiz,i.size,i.height, COUNT(i.id) AS num \n" +
            "FROM issuedsiz i, individual_protection_means s \n" +
            "WHERE s.id=i.siz_id AND \n" +
            "i.status = \"На складе\" AND \n" +
            "i.employee_id IS null AND \n" +
            "i.komplex_id IS null \n" +
            "GROUP BY 1,2,3,4,5", nativeQuery = true)
    List<Object[]> findGroupbySizeAndHeight();

    /**
     * <b>Поиск по сгруппированному списку не выданного СИЗ на складе предприятия</b>
     *
     * @param keyword ключевое слово поиска
     * @return сгруппированный по типу, размеру и росту список СИЗ на складе
     */
    @Query(value = "SELECT s.id,i.nomenclature_number,s.namesiz,i.size,i.height, COUNT(i.id) AS num \n" +
            "FROM issuedsiz i, individual_protection_means s \n" +
            "WHERE s.id=i.siz_id AND \n" +
            "i.status = \"На складе\" AND \n" +
            "i.employee_id IS null AND \n" +
            "i.komplex_id IS null AND\n" +
            "  (i.size LIKE %:keyword% OR\n" +
            "  i.height LIKE %:keyword% OR\n" +
            "  s.namesiz LIKE %:keyword% OR\n" +
            "  i.nomenclature_number LIKE %:keyword%)" +
            "GROUP BY 1,2,3,4,5", nativeQuery = true)
    List<Object[]> findGroupbySizeAndHeightByKeyword(@Param("keyword") String keyword);

    /**
     * <b>Получение сгруппированного списка не выданного СИЗ на складе подразделения</b>
     *
     * @param id ID подразделения
     * @return сгруппированный по типу, размеру и росту список СИЗ на складе
     */
    @Query(value = "SELECT s.id,i.nomenclature_number,s.namesiz,i.size,i.height, COUNT(i.id) AS num \n" +
            "FROM issuedsiz i, individual_protection_means s \n" +
            "WHERE s.id=i.siz_id AND \n" +
            "i.status = \"На складе\" AND \n" +
            "i.employee_id IS null AND \n" +
            "i.komplex_id = :id \n" +
            "GROUP BY 1,2,3,4,5", nativeQuery = true)
    List<Object[]> findGroupbySizeAndHeightByKomplex(@Param("id") Long id);

    /**
     * <b>Поиск по сгруппированному списку не выданного СИЗ на складе подразделения</b>
     *
     * @param id      ID подразделения
     * @param keyword ключевое слово поиска
     * @return сгруппированный по типу, размеру и росту список СИЗ на складе
     */
    @Query(value = "SELECT s.id,i.nomenclature_number,s.namesiz,i.size,i.height, COUNT(i.id) AS num \n" +
            "FROM issuedsiz i, individual_protection_means s \n" +
            "WHERE s.id=i.siz_id AND \n" +
            "i.status = \"На складе\" AND \n" +
            "i.employee_id IS null AND \n" +
            "i.komplex_id = :id AND \n" +
            "  (i.size LIKE %:keyword% OR\n" +
            "  i.height LIKE %:keyword% OR\n" +
            "  s.namesiz LIKE %:keyword% OR\n" +
            "  i.nomenclature_number LIKE %:keyword%)" +
            "GROUP BY 1,2,3,4,5", nativeQuery = true)
    List<Object[]> findGroupbySizeAndHeightByKomplexAndKeyword(@Param("id") Long id,
                                                               @Param("keyword") String keyword);

    /**
     * <b>Поиск СИЗ на складе подразделения</b>
     *
     * @param id      ID подразделения
     * @param keyword ключевое слово
     * @return список СИЗ
     */
    @Query(value = "SELECT i.* FROM issuedsiz i,individual_protection_means s\n" +
            "WHERE i.status = \"На складе\" AND\n" +
            "i.siz_id = s.id AND\n" +
            "i.komplex_id =:id AND " +
            "  (i.size LIKE %:keyword% OR\n" +
            "  i.height LIKE %:keyword% OR\n" +
            "  s.namesiz LIKE %:keyword% OR\n" +
            "  i.nomenclature_number LIKE %:keyword%)", nativeQuery = true)
    List<IssuedSIZ> findByStatusAndKomplexIdAndSizeLikeOrHeightLikeOrSizNameSIZLike(@Param("id") Long id,
                                                                                    @Param("keyword") String keyword);

    /**
     * Получить список СИЗ выданного сотруднику
     *
     * @param id     ID сотрудника
     * @param status статус СИЗ
     * @return список СИЗ отсортированный по дате окончания носки
     */
    List<IssuedSIZ> findAllByEmployeeIdAndStatusOrderByDateIssued(Long id, String status);

    /**
     * Получить СИЗ сотрудника с самой ближайшей датой окончания носки
     *
     * @param id ID сотрудника
     * @return СИЗ
     */
    @Query(value = "SELECT i.* FROM issuedsiz i \n" +
            "WHERE i.employee_id = :id AND \n" +
            "i.date_end_wear > CURRENT_DATE \n" +
            "ORDER BY i.date_end_wear LIMIT 1", nativeQuery = true)
    IssuedSIZ getByEndWearDateForEmployee(@Param("id") Long id);

    /**
     * Получить список СИЗ по норме выдачи
     *
     * @param id ID нормы выдачи
     * @return список СИЗ
     */
    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NOT NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " ipmstandard.siz_id = individual_protection_means.id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findIssuedByIPMStandart(@Param("id_ipm_st") Long id);

    /**
     * Получить список СИЗ (одежды) на складе соответствующего параметрам выбранного сотрудника и норме выдачи
     *
     * @param id_ipm ID нормы выдачи
     * @param id_emp ID сотрудника
     * @return Список СИЗ
     */
    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.clothing_size AND\n" +
            " issuedsiz.height LIKE employee.height AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForClothing(@Param("id_ipm_st") Long id_ipm,
                                                          @Param("emp_id") Long id_emp);

    /**
     * Получить список СИЗ (головные уборы) на складе соответствующего параметрам выбранного сотрудника и норме выдачи
     *
     * @param id_ipm ID нормы выдачи
     * @param id_emp ID сотрудника
     * @return Список СИЗ
     */
    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.headgear_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForHead(@Param("id_ipm_st") Long id_ipm,
                                                      @Param("emp_id") Long id_emp);

    /**
     * Получить список СИЗ (обувь) на складе соответствующего параметрам выбранного сотрудника и норме выдачи
     *
     * @param id_ipm ID нормы выдачи
     * @param id_emp ID сотрудника
     * @return Список СИЗ
     */
    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.shoe_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForShoe(@Param("id_ipm_st") Long id_ipm,
                                                      @Param("emp_id") Long id_emp);

    /**
     * Получить список СИЗ (противогаз) на складе соответствующего параметрам выбранного сотрудника и норме выдачи
     *
     * @param id_ipm ID нормы выдачи
     * @param id_emp ID сотрудника
     * @return Список СИЗ
     */
    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.gas_mask_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForGasMask(@Param("id_ipm_st") Long id_ipm,
                                                         @Param("emp_id") Long id_emp);

    /**
     * Получить список СИЗ (перчатки) на складе соответствующего параметрам выбранного сотрудника и норме выдачи
     *
     * @param id_ipm ID нормы выдачи
     * @param id_emp ID сотрудника
     * @return Список СИЗ
     */
    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.glove_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForGlove(@Param("id_ipm_st") Long id_ipm,
                                                       @Param("emp_id") Long id_emp);

    /**
     * Получить список СИЗ (руковицы) на складе соответствующего параметрам выбранного сотрудника и норме выдачи
     *
     * @param id_ipm ID нормы выдачи
     * @param id_emp ID сотрудника
     * @return Список СИЗ
     */
    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.mittens_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForMittens(@Param("id_ipm_st") Long id_ipm,
                                                         @Param("emp_id") Long id_emp);

    /**
     * Получить список СИЗ (респираторы) на складе соответствующего параметрам выбранного сотрудника и норме выдачи
     *
     * @param id_ipm ID нормы выдачи
     * @param id_emp ID сотрудника
     * @return Список СИЗ
     */
    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.respirator_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForRespirator(@Param("id_ipm_st") Long id_ipm,
                                                            @Param("emp_id") Long id_emp);

    /**
     * Получить список СИЗ выданного выбранному сотруднику
     *
     * @param id_ipm ID нормы выдачи
     * @param id_emp ID сотрудника
     * @return Список СИЗ
     */
    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id = employee.id AND\n" +
            " issuedsiz.status LIKE \"Выдано\" AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findByEmployeeIdAndIPMStandart(@Param("id_ipm_st") Long id_ipm,
                                                   @Param("emp_id") Long id_emp);

    /**
     * Получить количество СИЗ выданного выбранному сотруднику
     *
     * @param id_ipm ID нормы выдачи
     * @param id_emp ID сотрудника
     * @return Список СИЗ
     */
    @Query(value = "SELECT COUNT(issuedsiz.id) FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id = employee.id AND\n" +
            " issuedsiz.status LIKE \"Выдано\" AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    int getCountByEmployeeIdAndIPMStandart(@Param("id_ipm_st") Long id_ipm,
                                           @Param("emp_id") Long id_emp);

    /**
     * Получить список СИЗ необходимого для закупки на следующий год для всего центра
     *
     * @param date1 дата начала следующего года
     * @param date2 дата конца следующего года
     * @return сгруппированный список СИЗ для закупки
     */
    @Query(value = "SELECT s.id, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.size = t.size AND\n" +
            "      issuedsiz.height=t.height AND\n" +
            "      issuedsiz.siz_id=s.id AND\n" +
            "      issuedsiz.employee_id IS NOT null AND\n" +
            "      issuedsiz.date_end_wear BETWEEN :date1 AND :date2 AND\n" +
            "      issuedsiz.status = \"Выдано\") as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,3,4,6 \n" +
            "ORDER BY 3,4,5", nativeQuery = true)
    List<Object[]> getAllSIZForPurchaseByDate(@Param("date1") String date1,
                                              @Param("date2") String date2);

    /**
     * Получить список СИЗ необходимого для закупки на следующий год для подразделения
     *
     * @param date1 дата начала следующего года
     * @param date2 дата конца следующего года
     * @param id    ID подразделения
     * @return сгруппированный список СИЗ для закупки
     */
    @Query(value = "SELECT s.id, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.size = t.size AND\n" +
            "      issuedsiz.height=t.height AND\n" +
            "      issuedsiz.siz_id=s.id AND\n" +
            "      issuedsiz.employee_id = e.id AND\n" +
            "      issuedsiz.date_end_wear BETWEEN :date1 AND :date2 AND\n" +
            "      issuedsiz.status = \"Выдано\") as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "e.komplex_id =:id AND\n" +
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,3,4,6 \n" +
            "ORDER BY 3,4,5", nativeQuery = true)
    List<Object[]> getAllSIZForPurchaseByDateAndKomplex(@Param("date1") String date1,
                                                        @Param("date2") String date2,
                                                        @Param("id") Long id);

    /**
     * Получить список СИЗ необходимого для до закупки сотрудникам для всего центра
     *
     * @return сгруппированный список СИЗ для закупки
     */
    @Query(value = "SELECT s.id, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.size = t.size AND\n" +
            "      issuedsiz.height=t.height AND\n" +
            "      issuedsiz.siz_id=s.id AND\n" +
            "      issuedsiz.employee_id IS NOT null AND\n" +
            "      issuedsiz.status = \"Выдано\") as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,3,4 \n" +
            "ORDER BY 3,4,5", nativeQuery = true)
    List<Object[]> getAllSIZForPurchaseByNow();

    /**
     * Получить список СИЗ необходимого для до закупки сотрудникам для подразделения
     *
     * @param id ID подразделения
     * @return сгруппированный список СИЗ для закупки
     */
    @Query(value = "SELECT s.id, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.size = t.size AND\n" +
            "      issuedsiz.height=t.height AND\n" +
            "      issuedsiz.siz_id=s.id AND\n" +
            "      issuedsiz.employee_id = e.id AND\n" +
            "      issuedsiz.status = \"Выдано\") as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "e.komplex_id =:id AND\n" +
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,3,4,6 \n" +
            "ORDER BY 3,4,5", nativeQuery = true)
    List<Object[]> getAllSIZForPurchaseByNowAndKomplex(@Param("id") Long id);

    /**
     * Получить весь список СИЗ для закупки
     *
     * @return список СИЗ
     */
    @Query(value = "SELECT s.id, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.id = 0) as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,3,4 \n" +
            "ORDER BY 3,4,5", nativeQuery = true)
    List<Object[]> getAllSIZForPurchase();

    /**
     * Получить весь список СИЗ для закупки для подразделения
     *
     * @param id ID подразделения
     * @return список СИЗ
     */
    @Query(value = "SELECT s.id, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.id = 0) as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "e.komplex_id =:id AND\n" +
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,3,4 \n" +
            "ORDER BY 3,4,5", nativeQuery = true)
    List<Object[]> getAllSIZForPurchaseByKomplex(@Param("id") Long id);

    /**
     * Получить закупочную таблицу для всего подразделения
     *
     * @param id ID подразделения
     * @return список СИЗ
     */
    @Query(value = "SELECT s.id, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as num\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "e.komplex_id = :id AND\n" +
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "   t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,3,4\n" +
            "ORDER BY 3,4,5", nativeQuery = true)
    List<Object[]> getIssuedSIZForKomplex(@Param("id") Long id);

    /**
     * Получить список СИЗ по параметрам
     *
     * @param size        размер
     * @param height      рост
     * @param siz_id      ID типа СИЗ
     * @param status      статус
     * @param komplex_id  ID подразделения
     * @param employee_id ID сотрудника
     * @return список СИЗ
     */
    List<IssuedSIZ> findBySizeAndHeightAndSizIdAndStatusAndKomplexIdAndEmployeeId(String size, String height, Long siz_id, String status, Long komplex_id, Long employee_id);

    /**
     * Получить список не выданного СИЗ на складе предприятия по параметрам
     *
     * @param size   размер
     * @param height рост
     * @param siz_id ID типа СИЗ
     * @param status статус
     * @return список СИЗ
     */
    @Query(value = "SELECT * FROM issuedsiz WHERE \n" +
            "size =:s AND\n" +
            "height =:h AND\n" +
            "siz_id =:s_id AND\n" +
            "status =:stat AND\n" +
            "nomenclature_number =:n_n AND\n" +
            "komplex_id IS NULL AND\n" +
            "employee_id IS NULL", nativeQuery = true)
    List<IssuedSIZ> findByStock(@Param("s") String size,
                                @Param("h") String height,
                                @Param("s_id") long siz_id,
                                @Param("n_n") String nomenclatureNumber,
                                @Param("stat") String status);

    /**
     * Получить список не выданного СИЗ (не одежды) на складе предприятия по параметрам
     *
     * @param size   размер
     * @param siz_id ID типа СИЗ
     * @param status статус
     * @return список СИЗ
     */
    @Query(value = "SELECT * FROM issuedsiz WHERE \n" +
            "size =:s AND\n" +
            "siz_id =:s_id AND\n" +
            "nomenclature_number =:n_n AND\n" +
            "status =:stat AND\n" +
            "komplex_id IS NULL AND\n" +
            "employee_id IS NULL", nativeQuery = true)
    List<IssuedSIZ> findByStock(@Param("s") String size,
                                @Param("s_id") long siz_id,
                                @Param("n_n") String nomenclatureNumber,
                                @Param("stat") String status);

    /**
     * Получить список не выданного СИЗ на складе подразделения по параметрам
     *
     * @param size   размер
     * @param height рост
     * @param siz_id ID типа СИЗ
     * @param status статус
     * @param k_id   ID подразделения
     * @return список СИЗ
     */
    @Query(value = "SELECT * FROM issuedsiz WHERE \n" +
            "size =:s AND\n" +
            "height =:h AND\n" +
            "siz_id =:s_id AND\n" +
            "nomenclature_number =:n_n AND\n" +
            "status =:stat AND\n" +
            "komplex_id =:k_id AND\n" +
            "employee_id IS NULL", nativeQuery = true)
    List<IssuedSIZ> findByStockAndKomplex(@Param("s") String size,
                                          @Param("h") String height,
                                          @Param("s_id") long siz_id,
                                          @Param("stat") String status,
                                          @Param("n_n") String nomenclatureNumber,
                                          @Param("k_id") long k_id);

    /**
     * Получить список не выданного СИЗ (не одежды) на складе подразделения по параметрам
     *
     * @param size   размер
     * @param siz_id ID типа СИЗ
     * @param status статус
     * @param k_id   ID подразделения
     * @return список СИЗ
     */
    @Query(value = "SELECT * FROM issuedsiz WHERE \n" +
            "size =:s AND\n" +
            "height IS NULL AND\n" +
            "siz_id =:s_id AND\n" +
            "status =:stat AND\n" +
            "nomenclature_number =:n_n AND\n" +
            "komplex_id =:k_id AND\n" +
            "employee_id IS NULL", nativeQuery = true)
    List<IssuedSIZ> findByStockAndKomplex(@Param("s") String size,
                                          @Param("s_id") long siz_id,
                                          @Param("stat") String status,
                                          @Param("n_n") String nomenclatureNumber,
                                          @Param("k_id") long k_id);

    /**
     * <b>Выбор списка СИЗ по статусу и окончанию срока носки в выбранном диапазоне дат</b>
     *
     * @param status статус
     * @param date1  дата начала временного диапазона
     * @param date2  дата окончания временного диапазона
     * @return список СИЗ
     */
    @Query(value = "SELECT * FROM issuedsiz \n" +
            "WHERE status =:stat AND date_end_wear BETWEEN :date1 AND :date2", nativeQuery = true)
    List<IssuedSIZ> findByStatusWithEndingDateWearForSelectDate(@Param("stat") String status,
                                                                @Param("date1") String date1,
                                                                @Param("date2") String date2);
    /**
     * <b>Выбор списка СИЗ по статусу и с просроченным сроком носки</b>
     *
     * @param status статус
     * @param date1  дата
     * @return список СИЗ
     */
    @Query(value = "SELECT * FROM issuedsiz \n" +
            "WHERE status =:stat AND date_end_wear < :date1 ORDER BY date_end_wear", nativeQuery = true)
    List<IssuedSIZ> findByStatusWithEndingDateWear(@Param("stat") String status,
                                                                @Param("date1") String date1);
    /**
     * <b>Выбор списка СИЗ по статусу и подразделению с просроченным сроком носки</b>
     *
     * @param status статус
     * @param date1  дата
     * @param komplex_id ID подразделения
     * @return список СИЗ
     */
    @Query(value = "SELECT * FROM issuedsiz \n" +
            "LEFT JOIN employee ON issuedsiz.employee_id = employee.id\n" +
            "WHERE\n" +
            "    issuedsiz.status = :status AND employee.komplex_id = :id AND\n" +
            "    issuedsiz.date_end_wear < :date1 ORDER BY issuedsiz.date_end_wear", nativeQuery = true)
    List<IssuedSIZ> findByStatusAndKomplexWithEndingDateWear(@Param("status") String status,
                                                             @Param("date1") String date1,
                                                             @Param("id") Long komplex_id);
    /**
     * <b>Выбор списка СИЗ по статусу и подразделению с окончанием срока носки в выбранном диапазоне дат</b>
     *
     * @param status статус
     * @param date1  дата1
     * @param date2  дата2
     * @param komplex_id ID подразделения
     * @return список СИЗ
     */
    @Query(value = "SELECT * FROM issuedsiz \n" +
            "LEFT JOIN employee ON issuedsiz.employee_id = employee.id\n" +
            "WHERE\n" +
            "    issuedsiz.status = :status AND employee.komplex_id = :id AND\n" +
            "    issuedsiz.date_end_wear BETWEEN :date1 AND :date2 ORDER BY issuedsiz.date_end_wear", nativeQuery = true)
    List<IssuedSIZ> findByStatusAndKomplexWithEndingDateWearForSelectDate(@Param("status") String status,
                                                                          @Param("date1") String date1,
                                                                          @Param("date2") String date2,
                                                                          @Param("id") Long komplex_id);

}
