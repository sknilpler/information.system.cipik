package com.information.system.cipik.repo;


import com.information.system.cipik.models.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemsRepository extends CrudRepository<Item, Long> {
    Item findByName(String name);

    /**
     * Поиск на складе МЦ по соответствующим критериям
     *
     * @param name наименование МЦ
     * @param unit единицы измерения
     * @param code номенклатурный номер
     * @param id   ID статьи
     * @return МЦ (Item)
     */
    Item findByNameAndUnitAndCodeAndArticleId(String name, String unit, String code, long id);

    /**
     * Поиск на складе МЦ по количеству
     * @param number количество
     * @return список МЦ
     */
    List<Item> findAllByNumber(double number);

    /**
     * Поиск МЦ на складе
     * @return список мц
     */
    @Query(value = "select * from item where number > 0", nativeQuery = true)
    List<Item> findAllNotEmpty();


}
