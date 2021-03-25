package com.information.system.cipik.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Сущность нормы СИЗ
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class IPMStandard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //должность
    @OneToOne
    private Post post;
    //СИЗ
    @ManyToOne
    private IndividualProtectionMeans individualProtectionMeans;
    //норма выдачи
    private int issuanceRate;
    //срок службы
    private int serviceLife;
    //регламентирующие документы
    private String regulatoryDocuments;

    public IPMStandard(Post post, IndividualProtectionMeans individualProtectionMeans, int issuanceRate, int serviceLife, String regulatoryDocuments) {
        this.post = post;
        this.individualProtectionMeans = individualProtectionMeans;
        this.issuanceRate = issuanceRate;
        this.serviceLife = serviceLife;
        this.regulatoryDocuments = regulatoryDocuments;
    }

}
