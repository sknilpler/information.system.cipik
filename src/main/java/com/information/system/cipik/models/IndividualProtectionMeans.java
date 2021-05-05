package com.information.system.cipik.models;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "individual_protection_means_id")
    private List<IPMStandard> ipmStandards;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "siz_id")
    private List<IssuedSIZ> issuedSIZS;

    public IndividualProtectionMeans(String nameSIZ, String ed_izm, String typeIPM) {
        this.nameSIZ = nameSIZ;
        this.ed_izm = ed_izm;
        this.typeIPM = typeIPM;
    }
}
