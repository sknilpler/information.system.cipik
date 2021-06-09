package com.information.system.cipik.repo;

import com.information.system.cipik.models.IndividualProtectionMeans;
import com.information.system.cipik.utils.IPMByPost;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SIZRepository extends CrudRepository<IndividualProtectionMeans, Long> {

    /**
     * Получить список СИЗ по выбранной должности и норме выдачи
     *
     * @param id ID должности
     * @return список СИЗ
     */
    @Query(value = "select individual_protection_means.namesiz,individual_protection_means.ed_izm,ipmstandard.issuance_rate \n" +
            "from individual_protection_means,ipmstandard \n" +
            "WHERE individual_protection_means.id = ipmstandard.individual_protection_means_id \n" +
            "and ipmstandard.post_id = :idPost", nativeQuery = true)
    Iterable<IPMByPost> findAllByPostId(@Param("idPost") Long id);
}
