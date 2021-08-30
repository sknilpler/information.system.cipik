package com.information.system.cipik.repo;

import com.information.system.cipik.models.Coming;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ComingRepository extends CrudRepository<Coming,Long> {

    List<Coming> findAllByItemId(Long id);

    List<Coming> findAllByOrderByDateOfReceiveDesc();

  //  List<Coming> findAllByItemIdByOrderByDateOfReceive(Long id);

    /**
     * Фильтрация записей по диапазону даты приемки
     *
     * @param date1 Начальная дата диапазона
     * @param date2 Конечная дата диапазона
     * @return список приходов (Coming)
     */
    @Query(value = "SELECT c.*\n" +
            "FROM coming c\n" +
            "WHERE c.date_of_receive BETWEEN :date1 AND :date2 ORDER BY c.date_of_receive DESC",nativeQuery = true)
    Iterable<Coming> findAllBetweenDates(@Param("date1") Date date1,
                                         @Param("date2") Date date2);

    /**
     * Поиск приходов по ключевому слову
     *
     * @param keyword ключевое слово для поиска
     * @return список приходов (Coming)
     */
    @Query(value = "SELECT c.*\n" +
            "FROM coming c\n" +
            "WHERE\n" +
            "   c.bill LIKE %:keyword% OR\n" +
            "   c.name LIKE %:keyword% OR\n" +
            "   c.date_of_receive LIKE %:keyword% OR\n" +
            "   c.price LIKE %:keyword% OR\n" +
            "   c.unit LIKE %:keyword% OR\n" +
            "   c.nomenclature LIKE %:keyword% " +
            "ORDER BY c.date_of_receive DESC", nativeQuery = true)
    Iterable<Coming> searchComingsByKeyword(@Param("keyword") String keyword);

    /**
     * Поиск приходов по ключевому слову и фильтрацией по дате
     *
     * @param keyword ключевое слово для поиска
     * @return список приходов (Coming)
     */
    @Query(value = "SELECT c.*\n" +
            "FROM coming c\n" +
            "WHERE\n" +
            "   (c.bill LIKE %:keyword% OR\n" +
            "   c.name LIKE %:keyword% OR\n" +
            "   c.date_of_receive LIKE %:keyword% OR\n" +
            "   c.price LIKE %:keyword% OR\n" +
            "   c.unit LIKE %:keyword% OR\n" +
            "   c.nomenclature LIKE %:keyword%) AND c.date_of_receive BETWEEN :date1 AND :date2 " +
            "ORDER BY c.date_of_receive DESC", nativeQuery = true)
    Iterable<Coming> searchComingsByKeywordAndDate(@Param("keyword") String keyword,
                                                   @Param("date1") Date date1,
                                                   @Param("date2") Date date2);

}
