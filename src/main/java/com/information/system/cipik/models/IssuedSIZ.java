package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность выданный СИЗ
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class IssuedSIZ {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private IndividualProtectionMeans siz;

    //дата выдачи и окончания носки
    private Date dateIssued, dateEndWear;
    private String status;
    private String size;
    private String height;
    private String writeOffAct;
    private String nomenclatureNumber;

    @ManyToOne
    private Komplex komplex;

    public IssuedSIZ(IndividualProtectionMeans individualProtectionMeans, String size,String nomenclatureNumber) {
        this.siz = individualProtectionMeans;
        this.status = "На складе";
        this.size = size;
        this.nomenclatureNumber = nomenclatureNumber;
    }

    public IssuedSIZ(IndividualProtectionMeans individualProtectionMeans, String size, String height,String nomenclatureNumber) {
        this.siz = individualProtectionMeans;
        this.status = "На складе";
        this.size = size;
        this.height = height;
        this.nomenclatureNumber = nomenclatureNumber;
    }

    @Override
    public String toString() {
        return "IssuedSIZ{" +
                "id=" + id +
                ", employee=" + employee +
                ", siz=" + siz+
                ", dateIssued=" + dateIssued +
                ", dateEndWear=" + dateEndWear +
                ", status='" + status + '\'' +
                ", size='" + size + '\'' +
                ", height='" + height + '\'' +
                ", writeOffAct='" + writeOffAct + '\'' +
                ", nomenclatureNumber='" + nomenclatureNumber + '\'' +
                ", komplex=" + komplex +
                '}';
    }
}
