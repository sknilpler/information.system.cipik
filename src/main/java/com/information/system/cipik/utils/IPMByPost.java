package com.information.system.cipik.utils;

import lombok.Data;

@Data
public class IPMByPost {
    private String namesiz;
    private String ed_izm;
    private int issuanceRate;

    public IPMByPost(String namesiz, String ed_izm, int issuanceRate) {
        this.namesiz = namesiz;
        this.ed_izm = ed_izm;
        this.issuanceRate = issuanceRate;
    }
}
