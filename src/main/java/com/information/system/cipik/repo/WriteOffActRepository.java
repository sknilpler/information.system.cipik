package com.information.system.cipik.repo;

import com.information.system.cipik.models.WriteOffAct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WriteOffActRepository extends JpaRepository<WriteOffAct, Long> {

    List<WriteOffAct> findAllByItemId(Long id);

    List<WriteOffAct> findAllByOrderByDateAct();

}
