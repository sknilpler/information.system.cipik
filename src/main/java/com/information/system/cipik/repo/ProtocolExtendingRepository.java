package com.information.system.cipik.repo;

import com.information.system.cipik.models.ProtocolExtending;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProtocolExtendingRepository extends CrudRepository<ProtocolExtending, Long> {

    /**
     * Получить протоколы продления для определенного СИЗ
     * @param id ID СИЗ
     * @return список протоколов
     */
    List<ProtocolExtending>  findAllByIssuedSIZId(long id);
}
