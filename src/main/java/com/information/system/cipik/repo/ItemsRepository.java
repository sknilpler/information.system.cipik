package com.information.system.cipik.repo;


import com.information.system.cipik.models.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemsRepository extends CrudRepository<Item, Long> {
    Item findByName(String name);

    /**
     * Поиск на складе МЦ по соответствующим критериям
     *
     * @param name наименование МЦ
     * @param unit единицы измерения
     * @param id   ID статьи
     * @return МЦ (Item)
     */
    Item findByNameAndUnitAndNomenclatureAndArticleId(String name, String unit, String nomenclature, long id);

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

    /**
     * Поиск по таблице склада
     *
     * @param keyword ключевое слово поиска
     * @return список МЦ
     */
    @Query(value = "SELECT\n" +
            "   i.*\n" +
            "FROM\n" +
            "   item i,\n" +
            "   article a\n" +
            "WHERE\n" +
            "   CONCAT(\n" +
            "       i.bill,\n" +
            "       i.code,\n" +
            "       i.name,\n" +
            "       i.nomenclature,\n" +
            "       i.number,\n" +
            "       i.unit,\n" +
            "       i.price,\n" +
            "       a.name\n" +
            "   ) LIKE %:keyword% AND i.article_id = a.id AND i.number > 0",nativeQuery = true)
    List<Item> searchingItemsByKeyword(@Param("keyword") String keyword);

}
