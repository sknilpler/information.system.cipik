package com.information.system.cipik.repo;

import com.information.system.cipik.models.Komplex;
import org.springframework.data.repository.CrudRepository;

public interface KomplexRepository extends CrudRepository<Komplex, Long> {

    /**
     * Найти подразделение по аббревиатуре
     *
     * @param shortName аббревиатура
     * @return комплекс
     */
    Komplex findByShortName(String shortName);

    /**
     * найти комплекс по роли пользователя
     *
     * @param id ID роли
     * @return комплекс
     */
    Komplex findByRoleId(Long id);
}
