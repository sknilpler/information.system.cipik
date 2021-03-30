package com.information.system.cipik.repo;

import com.information.system.cipik.models.IssuedSIZ;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IssuedSIZRepository extends CrudRepository<IssuedSIZ,Long> {

    List<IssuedSIZ> findAllByEmployeeId(Long id);
}
