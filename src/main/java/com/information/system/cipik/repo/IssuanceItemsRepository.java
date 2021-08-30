package com.information.system.cipik.repo;

import com.information.system.cipik.models.IssuanceItems;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IssuanceItemsRepository extends CrudRepository<IssuanceItems,Long> {

    List<IssuanceItems> findAllByItemId(Long id);

    List<IssuanceItems> findAllByOrderByDateIssuedDesc();

    /**
     * Поиск выданного МЦ по ключевому слову
     *
     * @param keyword ключевое слово
     * @return список МЦ (List<IssuanceItems>)
     */
    @Query(value = "SELECT\n" +
            "    ii.*\n" +
            "FROM\n" +
            "    issuance_items ii,\n" +
            "    item i,\n" +
            "    komplex k\n" +
            "WHERE\n" +
            "    CONCAT(\n" +
            "        ii.employee_accepting,\n" +
            "        ii.employee_gave_out,\n" +
            "        ii.invoice_number,\n" +
            "        ii.number,\n" +
            "        i.name,\n" +
            "        k.short_name\n" +
            ") LIKE %:keyword% AND ii.item_id = i.id AND ii.komplex_id = k.id ORDER BY ii.date_issued DESC",nativeQuery = true)
    List<IssuanceItems> searchByKeyword(@Param("keyword") String keyword);

    /**
     * Фильтрация списка выдачей МЦ по дате
     *
     * @param date1 дата начала фильтрации
     * @param date2 дата конца фильтрации
     * @return список МЦ (Iterable<IssuanceItems>)
     */
    @Query(value = "SELECT i.*\n" +
            "FROM issuance_items i\n" +
            "WHERE i.date_issued BETWEEN :date1 AND :date2 ORDER BY i.date_issued DESC",nativeQuery = true)
    Iterable<IssuanceItems> findAllBetweenDates(@Param("date1") Date date1,
                                                @Param("date2") Date date2);

    /**
     * Поиск и фильтрация выданного МЦ по дате и ключевому слову
     *
     * @param keyword ключевое слово
     * @param date1 дата начала фильтрации
     * @param date2 дата конца фильтрации
     * @return список МЦ (Iterable<IssuanceItems>)
     */
    @Query(value = "SELECT\n" +
            "    ii.*\n" +
            "FROM\n" +
            "    issuance_items ii,\n" +
            "    item i,\n" +
            "    komplex k\n" +
            "WHERE\n" +
            "    CONCAT(\n" +
            "        ii.employee_accepting,\n" +
            "        ii.employee_gave_out,\n" +
            "        ii.invoice_number,\n" +
            "        ii.number,\n" +
            "        i.name,\n" +
            "        k.short_name\n" +
            ") LIKE %:keyword% AND ii.item_id = i.id AND ii.komplex_id = k.id AND ii.date_issued BETWEEN :date1 AND :date2 ORDER BY ii.date_issued DESC",nativeQuery = true)
    Iterable<IssuanceItems> searchByKeywordAndBetweenDates(@Param("keyword") String keyword,
                                                           @Param("date1") Date date1,
                                                           @Param("date2") Date date2);
}
