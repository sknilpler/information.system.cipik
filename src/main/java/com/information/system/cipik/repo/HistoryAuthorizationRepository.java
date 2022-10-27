package com.information.system.cipik.repo;

import com.information.system.cipik.models.HistoryAuthorization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;

public interface HistoryAuthorizationRepository extends CrudRepository<HistoryAuthorization, Long> {

    List<HistoryAuthorization> findAllByOrderByAuthorizationTimeDesc();

    @Query(value = "SELECT * FROM history_authorization " +
            "WHERE authorization_time >= :t1 AND " +
            "authorization_time <= :t2 " +
            "ORDER BY 1 DESC", nativeQuery = true)
    List<HistoryAuthorization> findAllBySelectedDateOrderByAuthorizationTimeDesc(@Param("t1") Date t1,
                                                                                 @Param("t2") Date t2);

    @Query(value = "SELECT * FROM history_authorization \n" +
            "WHERE authorization_time >= :t1 AND \n" +
            "authorization_time <= :t2 AND \n" +
            "username LIKE %:u% \n" +
            "ORDER BY 1 DESC;", nativeQuery = true)
    List<HistoryAuthorization> findAllBySelectedDateAndUsernameOrderByAuthorizationTimeDesc(@Param("t1") Date t1,
                                                                                            @Param("t2") Date t2,
                                                                                            @Param("u") String u);
}
