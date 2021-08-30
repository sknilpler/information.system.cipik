package com.information.system.cipik.repo;

import com.information.system.cipik.models.WriteOffAct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface WriteOffActRepository extends JpaRepository<WriteOffAct, Long> {

    List<WriteOffAct> findAllByItemId(Long id);

    List<WriteOffAct> findAllByOrderByDateActDesc();


    /**
     * Поиск актов списания по ключевому слову
     *
     * @param keyword ключевое слово
     * @return список актов списания (List<WriteOffAct>)
     */
    @Query(value = "SELECT w.*\n" +
            "FROM write_off_act w, komplex k, item i\n" +
            "WHERE CONCAT(w.name_act,i.name,k.short_name) LIKE %:keyword% AND w.komplex_id = k.id AND w.item_id = i.id ORDER BY w.date_act DESC", nativeQuery = true)
    List<WriteOffAct> searchByKeyword(@Param("keyword") String keyword);


    /**
     * Фильтрация списка актов списания по дате
     *
     * @param date1 первоначальная дата
     * @param date2 конечная дата
     * @return список актов списания (Iterable<WriteOffAct>)
     */
    @Query(value = "SELECT * FROM write_off_act \n" +
            "WHERE date_act BETWEEN :date1 AND :date2\n" +
            "ORDER BY date_act DESC", nativeQuery = true)
    Iterable<WriteOffAct> findAllBetweenDates(@Param("date1") Date date1,
                        @Param("date2") Date date2);


    /**
     * Поиск и фильтрация списка актов списания
     *
     * @param keyword ключевое слово
     * @param date1 первоначальная дата
     * @param date2 конечная дата
     * @return список актов списания (Iterable<WriteOffAct>)
     */
    @Query(value = "SELECT\n" +
            "    w.*\n" +
            "FROM\n" +
            "    write_off_act w,\n" +
            "    komplex k,\n" +
            "    item i\n" +
            "WHERE\n" +
            "    CONCAT(\n" +
            "        w.name_act,\n" +
            "        i.name,\n" +
            "        k.short_name\n" +
            "    ) LIKE %:keyword% AND w.komplex_id = k.id AND w.item_id = i.id AND w.date_act BETWEEN :date1 AND :date2\n" +
            "ORDER BY\n" +
            "    w.date_act DESC", nativeQuery = true)
    Iterable<WriteOffAct> searchByKeywordAndBetweenDates(@Param("keyword") String keyword,
                                                         @Param("date1") Date date1,
                                                         @Param("date2") Date date2);
}
