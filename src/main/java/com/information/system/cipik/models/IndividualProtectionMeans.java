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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "siz_id")
    private List<IPMStandard> ipmStandards;

}
