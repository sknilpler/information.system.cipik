package com.information.system.cipik.repo;

import com.information.system.cipik.models.IPMStandard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPMStandardRepository extends CrudRepository<IPMStandard, Long> {

    /**
     * Получить список норм выдачи по выбранной должности
     *
     * @param id ID должности
     * @return список норм выдачи
     */
    List<IPMStandard> findAllByPostId(Long id);

    /**
     * Получить количество не выданного СИЗ на складе предприятия
     *
     * @param id ID типа СИЗ
     * @return количество СИЗ выбранного типа на складе
     */
    @Query(value = "SELECT COUNT(*) FROM issuedsiz,ipmstandard \n" +
            "WHERE issuedsiz.siz_id = ipmstandard.individual_protection_means_id AND \n" +
            "issuedsiz.status = \"На складе\" AND\n" +
            "ipmstandard.id = :id_ipm AND\n" +
            " issuedsiz.komplex_id IS NULL", nativeQuery = true)
    int notIssuanceRate(@Param("id_ipm") Long id);

    /**
     * Получить количество не выданного СИЗ на складе подразделения
     *
     * @param id_ipm     ID типа СИЗ
     * @param id_komplex ID подразделения
     * @return количество СИЗ выбранного типа на складе
     */
    @Query(value = "SELECT COUNT(*) FROM issuedsiz,ipmstandard \n" +
            "WHERE issuedsiz.siz_id = ipmstandard.individual_protection_means_id AND \n" +
            "issuedsiz.status = \"На складе\" AND\n" +
            "ipmstandard.id = :id_ipm AND\n" +
            "issuedsiz.komplex_id = :id_komplex", nativeQuery = true)
    int notIssuanceRateForKomplex(@Param("id_ipm") Long id_ipm,
                                  @Param("id_komplex") Long id_komplex);
}
