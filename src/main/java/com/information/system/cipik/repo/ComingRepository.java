package com.information.system.cipik.repo;

import com.information.system.cipik.models.Coming;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ComingRepository extends CrudRepository<Coming,Long> {

    List<Coming> findAllByItemId(Long id);


}
