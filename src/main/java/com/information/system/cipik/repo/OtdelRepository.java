package com.information.system.cipik.repo;

import com.information.system.cipik.models.Otdel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OtdelRepository extends CrudRepository<Otdel,Long> {

    List<Otdel> findAllByKomplexId(Long id);
}
