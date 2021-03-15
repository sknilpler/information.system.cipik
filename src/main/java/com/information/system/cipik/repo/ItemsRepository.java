package com.information.system.cipik.repo;


import com.information.system.cipik.models.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemsRepository extends CrudRepository<Item, Long> {
    Item findByName(String name);
}
