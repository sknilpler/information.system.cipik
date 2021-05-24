package com.information.system.cipik.repo;

import com.information.system.cipik.models.IssuedSIZ;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssuedSIZRepository extends CrudRepository<IssuedSIZ,Long> {

    List<IssuedSIZ> findAllByEmployeeId(Long id);
    List<IssuedSIZ> findByStatus(String status);
    List<IssuedSIZ> findByStatusAndKomplexId(String status,Long komplex_id);
    List<IssuedSIZ> findByStatusAndSizeLikeOrHeightLikeOrSizNameSIZLike(String status, String size, String height, String nameSIZ);
    //List<IssuedSIZ> findByStatusAndKomplexIdAndSizeLikeOrHeightLikeOrSizNameSIZLike(String status,Long komplex_id, String size, String height, String nameSIZ);

    @Query(value = "SELECT i.* FROM issuedsiz i,individual_protection_means s\n" +
            "WHERE i.status = \"На складе\" AND\n" +
            "i.siz_id = s.id AND\n"+
            "i.komplex_id =:id AND CONCAT(i.size,i.height,s.namesiz,s.nomenclature_number) LIKE %:keyword%", nativeQuery = true)
    List<IssuedSIZ> findByStatusAndKomplexIdAndSizeLikeOrHeightLikeOrSizNameSIZLike(@Param("id") Long id, @Param("keyword") String keyword);

    List<IssuedSIZ> findAllByEmployeeIdAndStatusOrderByDateIssued(Long id, String status);

    @Query(value = "SELECT i.* FROM issuedsiz i \n" +
            "WHERE i.employee_id = :id AND \n" +
            "i.date_end_wear > CURRENT_DATE \n" +
            "ORDER BY i.date_end_wear LIMIT 1", nativeQuery = true)
    IssuedSIZ getByEndWearDateForEmployee(@Param("id") Long id);

    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NOT NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " ipmstandard.siz_id = individual_protection_means.id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findIssuedByIPMStandart(@Param("id_ipm_st") Long id);

    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.clothing_size AND\n" +
            " issuedsiz.height LIKE employee.height AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForClothing(@Param("id_ipm_st") Long id_ipm,@Param("emp_id") Long id_emp);

    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.headgear_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForHead(@Param("id_ipm_st") Long id_ipm,@Param("emp_id") Long id_emp);

    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.shoe_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForShoe(@Param("id_ipm_st") Long id_ipm,@Param("emp_id") Long id_emp);

    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.gas_mask_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForGasMask(@Param("id_ipm_st") Long id_ipm,@Param("emp_id") Long id_emp);

    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.glove_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForGlove(@Param("id_ipm_st") Long id_ipm,@Param("emp_id") Long id_emp);

    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.mittens_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForMittens(@Param("id_ipm_st") Long id_ipm,@Param("emp_id") Long id_emp);

    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id IS NULL AND\n" +
            " issuedsiz.status LIKE \"На складе\" AND\n" +
            " issuedsiz.size LIKE employee.respirator_size AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findNotIssuedByIPMStandartForRespirator(@Param("id_ipm_st") Long id_ipm,@Param("emp_id") Long id_emp);

    @Query(value = "SELECT issuedsiz.* FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id = employee.id AND\n" +
            " issuedsiz.status LIKE \"Выдано\" AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    List<IssuedSIZ> findByEmployeeIdAndIPMStandart(@Param("id_ipm_st") Long id_ipm,@Param("emp_id") Long id_emp);

    @Query(value = "SELECT COUNT(issuedsiz.id) FROM issuedsiz,individual_protection_means,ipmstandard,employee WHERE\n" +
            " issuedsiz.siz_id = individual_protection_means.id AND\n" +
            " issuedsiz.employee_id = employee.id AND\n" +
            " issuedsiz.status LIKE \"Выдано\" AND\n" +
            " ipmstandard.individual_protection_means_id = individual_protection_means.id AND\n" +
            " employee.id = :emp_id AND\n" +
            " ipmstandard.id = :id_ipm_st", nativeQuery = true)
    int getCountByEmployeeIdAndIPMStandart(@Param("id_ipm_st") Long id_ipm,@Param("emp_id") Long id_emp);

    @Query(value = "SELECT s.id, s.nomenclature_number, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.size = t.size AND\n" +
            "      issuedsiz.height=t.height AND\n" +
            "      issuedsiz.siz_id=s.id AND\n" +
            "      issuedsiz.employee_id IS NOT null AND\n" +
            "      issuedsiz.date_end_wear BETWEEN :date1 AND :date2 AND\n"+
            "      issuedsiz.status = \"Выдано\") as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,4,5,7 \n"+
            "ORDER BY 3,4,5",nativeQuery = true)
    List<Object[]> getAllSIZForPurchaseByDate(@Param("date1") String date1,@Param("date2") String date2);

    @Query(value = "SELECT s.id, s.nomenclature_number, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.size = t.size AND\n" +
            "      issuedsiz.height=t.height AND\n" +
            "      issuedsiz.siz_id=s.id AND\n" +
            "      issuedsiz.employee_id = e.id AND\n" +
            "      issuedsiz.date_end_wear BETWEEN :date1 AND :date2 AND\n"+
            "      issuedsiz.status = \"Выдано\") as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "e.komplex_id =:id AND\n"+
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,4,5,7 \n"+
            "ORDER BY 3,4,5",nativeQuery = true)
    List<Object[]> getAllSIZForPurchaseByDateAndKomplex(@Param("date1") String date1,@Param("date2") String date2,@Param("id") Long id);


    @Query(value = "SELECT s.id,s.nomenclature_number, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.size = t.size AND\n" +
            "      issuedsiz.height=t.height AND\n" +
            "      issuedsiz.siz_id=s.id AND\n" +
            "      issuedsiz.employee_id IS NOT null AND\n" +
            "      issuedsiz.status = \"Выдано\") as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,4,5,7 \n"+
            "ORDER BY 3,4,5",nativeQuery = true)
    List<Object[]> getAllSIZForPurchaseByNow();


    @Query(value = "SELECT s.id,s.nomenclature_number, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.size = t.size AND\n" +
            "      issuedsiz.height=t.height AND\n" +
            "      issuedsiz.siz_id=s.id AND\n" +
            "      issuedsiz.employee_id = e.id AND\n" +
            "      issuedsiz.status = \"Выдано\") as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "e.komplex_id =:id AND\n"+
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,4,5,7 \n"+
            "ORDER BY 3,4,5",nativeQuery = true)
    List<Object[]> getAllSIZForPurchaseByNowAndKomplex(@Param("id") Long id);


    @Query(value = "SELECT s.id,s.nomenclature_number, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.id = 0) as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,4,5,7 \n"+
            "ORDER BY 3,4,5",nativeQuery = true)
    List<Object[]> getAllSIZForPurchase();

    @Query(value = "SELECT s.id,s.nomenclature_number, s.namesiz, t.height, t.size, SUM(i.issuance_rate) as numAll, \n" +
            "(SELECT COUNT(issuedsiz.id) FROM issuedsiz\n" +
            "WHERE issuedsiz.id = 0) as numIssued\n" +
            "FROM employee e, post p, ipmstandard i, individual_protection_means s, size_siz t\n" +
            "WHERE e.post_id = p.id AND\n" +
            "i.post_id = p.id AND\n" +
            "i.individual_protection_means_id = s.id AND\n" +
            "s.id = t.individual_protection_means_id AND\n" +
            "e.komplex_id =:id AND\n"+
            "IF(s.typeipm = \"Одежда\",\n" +
            "t.height = e.height AND\n" +
            "t.size = e.clothing_size,\n" +
            "IF(s.typeipm = \"Обувь\",\n" +
            "t.size = e.shoe_size,\n" +
            "       IF(s.typeipm = \"Головной убор\",\n" +
            "       t.size = e.headgear_size,\n" +
            "          IF(s.typeipm = \"Перчатки\",\n" +
            "          t.size = e.glove_size,\n" +
            "            IF(s.typeipm = \"Рукавицы\",\n" +
            "            t.size = e.mittens_size,\n" +
            "              IF(s.typeipm = \"Противогаз\",\n" +
            "              t.size = e.gas_mask_size,\n" +
            "                IF(s.typeipm = \"Респиратор\",\n" +
            "                t.size = e.respirator_size,NULL)))))))  \n" +
            "GROUP BY 1,4,5,7 \n"+
            "ORDER BY 3,4,5",nativeQuery = true)
    List<Object[]> getAllSIZForPurchaseByKomplex(@Param("id") Long id);
}
