package com.information.system.cipik.repo;

import com.information.system.cipik.models.Norma;
import org.springframework.data.repository.CrudRepository;

public interface NormaRepository extends CrudRepository<Norma,Long> {


    Iterable<Norma> findAllBySredstvoId(Long id);
}
