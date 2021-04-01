package com.information.system.cipik.repo;

import com.information.system.cipik.models.IPMStandard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPMStandardRepository extends CrudRepository<IPMStandard,Long> {

    List<IPMStandard> findAllByPostId(Long id);


    @Query(value = "SELECT COUNT(*) FROM issuedsiz,ipmstandard \n" +
            "WHERE issuedsiz.siz_id = ipmstandard.individual_protection_means_id AND \n" +
            "issuedsiz.status = \"На складе\" AND\n" +
            "ipmstandard.id = :id_ipm", nativeQuery = true)
    int notIssuanceRate(@Param("id_ipm") Long id);
}
