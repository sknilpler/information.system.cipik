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
    @ManyToOne
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
    private String typeIPM;

    public IPMStandard(Post post, IndividualProtectionMeans individualProtectionMeans, int issuanceRate, int serviceLife, String regulatoryDocuments,String typeIPM) {
        this.post = post;
        this.individualProtectionMeans = individualProtectionMeans;
        this.issuanceRate = issuanceRate;
        this.serviceLife = serviceLife;
        this.regulatoryDocuments = regulatoryDocuments;
        this.typeIPM = typeIPM;
    }

    @Override
    public String toString() {
        return "IPMStandard{" +
                "id=" + id +
                ", post=" + post +
                ", individualProtectionMeans=" + individualProtectionMeans +
                ", issuanceRate=" + issuanceRate +
                ", serviceLife=" + serviceLife +
                ", regulatoryDocuments='" + regulatoryDocuments + '\'' +
                ", typeIPM='" + typeIPM + '\'' +
                '}';
    }
}
