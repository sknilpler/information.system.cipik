package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SizeSiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //СИЗ
    @ManyToOne
    private IndividualProtectionMeans individualProtectionMeans;

    private String size;
    private String height;

    public SizeSiz(IndividualProtectionMeans individualProtectionMeans, String size, String height) {
        this.individualProtectionMeans = individualProtectionMeans;
        this.size = size;
        this.height = height;
    }
}
