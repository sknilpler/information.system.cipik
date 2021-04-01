package com.information.system.cipik.utils;

import com.information.system.cipik.models.IndividualProtectionMeans;
import lombok.Data;

@Data
public class IPMByPost {
    private Long id;
    private int issuanceRate;
    private IndividualProtectionMeans individualProtectionMeans;
    //норма выдачи
    private int notIssuanceNum;
    private int issuanceNum;

}
