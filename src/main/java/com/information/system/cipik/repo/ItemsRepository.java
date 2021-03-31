package com.information.system.cipik.repo;


import com.information.system.cipik.models.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemsRepository extends CrudRepository<Item, Long> {
    Item findByName(String name);


//    @Query(value = "select item.* FROM item,ipmstandard,individual_protection_means WHERE\n" +
//            " item.name LIKE individual_protection_means.namesiz AND\n" +
//            " item.issued = 0 AND\n" +
//            " individual_protection_means.id = ipmstandard.individual_protection_means_id AND\n" +
//            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
//    List<Item> findIssuedByIPMStandart(@Param("id_ipm_st") Long id);
}
