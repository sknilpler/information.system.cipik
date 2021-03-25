package com.information.system.cipik.repo;

import com.information.system.cipik.models.IPMStandard;
import org.springframework.data.repository.CrudRepository;

public interface IPMStandardRepository extends CrudRepository<IPMStandard,Long> {

    Iterable<IPMStandard> findAllByPostId(Long id);

}
