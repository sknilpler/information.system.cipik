package com.information.system.cipik.utils;

public class SIZForStock {
    private Long id;
    private String nameSIZ;
    private String ed_izm;
    private String typeIPM;
    private String nomenclatureNumber;

    private String incomeNumber;
    private String size;
    private String height;
    private int number;

    public SIZForStock() {
    }

    public SIZForStock(Long id, String nomenclatureNumber, String incomeNumber, String nameSIZ, String ed_izm, String typeIPM, String size, String height, int number) {
        this.id = id;
        this.nameSIZ = nameSIZ;
        this.ed_izm = ed_izm;
        this.typeIPM = typeIPM;
        this.nomenclatureNumber = nomenclatureNumber;
        this.size = size;
        this.height = height;
        this.number = number;
        this.incomeNumber = incomeNumber;
    }

    public SIZForStock(Long id, String nomenclatureNumber, String incomeNumber, String nameSIZ, String size, String height, int number) {
        this.id = id;
        this.nameSIZ = nameSIZ;
        this.nomenclatureNumber = nomenclatureNumber;
        this.size = size;
        this.height = height;
        this.number = number;
        this.incomeNumber = incomeNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameSIZ() {
        return nameSIZ;
    }

    public void setNameSIZ(String nameSIZ) {
        this.nameSIZ = nameSIZ;
    }

    public String getEd_izm() {
        return ed_izm;
    }

    public void setEd_izm(String ed_izm) {
        this.ed_izm = ed_izm;
    }

    public String getTypeIPM() {
        return typeIPM;
    }

    public void setTypeIPM(String typeIPM) {
        this.typeIPM = typeIPM;
    }

    public String getNomenclatureNumber() {
        return nomenclatureNumber;
    }

    public void setNomenclatureNumber(String nomenclatureNumber) {
        this.nomenclatureNumber = nomenclatureNumber;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getIncomeNumber() {
        return incomeNumber;
    }

    public void setIncomeNumber(String incomeNumber) {
        this.incomeNumber = incomeNumber;
    }
}
