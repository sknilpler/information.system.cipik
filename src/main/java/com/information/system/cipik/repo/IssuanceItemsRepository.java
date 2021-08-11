package com.information.system.cipik.repo;

import com.information.system.cipik.models.IssuanceItems;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IssuanceItemsRepository extends CrudRepository<IssuanceItems,Long> {

    List<IssuanceItems> findAllByItemId(Long id);

    List<IssuanceItems> findAllByOrderByDateIssued();

}
