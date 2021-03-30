package com.information.system.cipik.repo;

import com.information.system.cipik.models.IPMStandard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPMStandardRepository extends CrudRepository<IPMStandard,Long> {

    List<IPMStandard> findAllByPostId(Long id);

}
