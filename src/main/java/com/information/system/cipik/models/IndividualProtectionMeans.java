package com.information.system.cipik.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность СИЗ
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class IndividualProtectionMeans {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nameSIZ;
    private String ed_izm;
    private String typeIPM;
    private String nomenclatureNumber;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "individual_protection_means_id")
    private List<IPMStandard> ipmStandards;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "siz_id")
    private List<IssuedSIZ> issuedSIZS;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "individual_protection_means_id")
    @JsonIgnore
    private List<SizeSiz> sizeSizs;

    public IndividualProtectionMeans(String nameSIZ, String ed_izm, String typeIPM, String nomenclatureNumber) {
        this.nameSIZ = nameSIZ;
        this.ed_izm = ed_izm;
        this.typeIPM = typeIPM;
        this.nomenclatureNumber = nomenclatureNumber;
    }
}
