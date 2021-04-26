package com.information.system.cipik.repo;

import com.information.system.cipik.models.Komplex;
import org.springframework.data.repository.CrudRepository;

public interface KomplexRepository extends CrudRepository<Komplex,Long> {
    Komplex findByShortName(String shortName);
}
