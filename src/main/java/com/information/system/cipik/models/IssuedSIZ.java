package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.net.openssl.ciphers.Protocol;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

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

    private String incomeNumber;

    @ManyToOne
    private Komplex komplex;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "issuedSIZ_id")
    private List<ProtocolExtending> protocols;

    public IssuedSIZ(IndividualProtectionMeans individualProtectionMeans, String size,String nomenclatureNumber, String incomeNumber) {
        this.siz = individualProtectionMeans;
        this.status = "На складе";
        this.size = size;
        this.nomenclatureNumber = nomenclatureNumber;
        this.incomeNumber = incomeNumber;
    }

    public IssuedSIZ(IndividualProtectionMeans individualProtectionMeans, String size, String height,String nomenclatureNumber, String incomeNumber) {
        this.siz = individualProtectionMeans;
        this.status = "На складе";
        this.size = size;
        this.height = height;
        this.nomenclatureNumber = nomenclatureNumber;
        this.incomeNumber = incomeNumber;
    }

    public long getMonthOfIssued(){
        return ChronoUnit.MONTHS.between(
                YearMonth.from(this.dateIssued.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()),
                YearMonth.from(LocalDate.now())
        );
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
                ", incomeNumber='" + incomeNumber + '\'' +
                ", komplex=" + komplex +
                '}';
    }
}
