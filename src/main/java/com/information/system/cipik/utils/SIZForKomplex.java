package com.information.system.cipik.utils;

public class SIZForKomplex {
    private Long id;
    private String nomenclatureNumber;
    private String namesiz;
    private String height;
    private String size;
    private int number;

    public SIZForKomplex() {
    }

    @Override
    public String toString() {
        return "SIZForKomplex{" +
                "id=" + id +
                ", nomenclatureNumber='" + nomenclatureNumber + '\'' +
                ", namesiz='" + namesiz + '\'' +
                ", height='" + height + '\'' +
                ", size='" + size + '\'' +
                ", number=" + number +
                '}';
    }

    public SIZForKomplex(Long id, String nomenclatureNumber, String namesiz, String height, String size, int number) {
        this.id = id;
        this.nomenclatureNumber = nomenclatureNumber;
        this.namesiz = namesiz;
        this.height = height;
        this.size = size;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomenclatureNumber() {
        return nomenclatureNumber;
    }

    public void setNomenclatureNumber(String nomenclatureNumber) {
        this.nomenclatureNumber = nomenclatureNumber;
    }

    public String getNamesiz() {
        return namesiz;
    }

    public void setNamesiz(String namesiz) {
        this.namesiz = namesiz;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
