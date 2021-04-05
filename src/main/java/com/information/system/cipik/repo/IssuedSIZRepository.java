package com.information.system.cipik.repo;

import com.information.system.cipik.models.IssuedSIZ;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssuedSIZRepository extends CrudRepository<IssuedSIZ,Long> {

    List<IssuedSIZ> findAllByEmployeeId(Long id);
    List<IssuedSIZ> findByStatus(String status);
    List<IssuedSIZ> findByStatusAndSizeLikeOrHeightLikeOrSizNameSIZLike(String status, String size, String height, String nameSIZ);



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
}
