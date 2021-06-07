package com.information.system.cipik.repo;

import com.information.system.cipik.models.SizeSiz;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SizeSizRepository extends CrudRepository<SizeSiz,Long> {

    /**
     * Найти список размеров для выбранного типа СИЗ
     *
     * @param id ID СИЗ
     * @return список размеров
     */
    List<SizeSiz> findAllByIndividualProtectionMeansId(Long id);
}
