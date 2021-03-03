package com.information.system.cipik.repo;

import com.information.system.cipik.models.Sredstvo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SredstvoRepository extends CrudRepository<Sredstvo,Long> {

    @Query(value = "select * from sredstvo where name like %:keyword% or indeks like %:keyword%", nativeQuery = true)
    Iterable<Sredstvo> findAllByKeyword(@Param("keyword") String keyword);

}
