package com.information.system.cipik.utils;

public class SIZForPurchase {
    private Long id;
    private String namesiz;
    private String height;
    private String size;
    private int numAll;
    private int numIssued;
    private int numPurchase;

    public SIZForPurchase() {
    }

    public SIZForPurchase(Long id, String namesiz, String height, String size, int numAll, int numIssued) {
        this.id = id;
        this.namesiz = namesiz;
        this.height = height;
        this.size = size;
        this.numAll = numAll;
        this.numIssued = numIssued;
        this.numPurchase = numAll-numIssued;
    }

    public SIZForPurchase(Long id, String namesiz, String height, String size, int num) {
        this.id = id;
        this.namesiz = namesiz;
        this.height = height;
        this.size = size;
        this.numAll = num;
    }

    public Long getId() {
        return id;
    }

    public String getNamesiz() {
        return namesiz;
    }

    public void setNamesiz(String namesiz) {
        this.namesiz = namesiz;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getNumAll() {
        return numAll;
    }

    public void setNumAll(int numAll) {
        this.numAll = numAll;
    }

    public int getNumIssued() {
        return numIssued;
    }

    public void setNumIssued(int numIssued) {
        this.numIssued = numIssued;
    }

    public int getNumPurchase() {
        return numPurchase;
    }

    public void setNumPurchase(int numPurchase) {
        this.numPurchase = numPurchase;
    }

    @Override
    public String toString() {
        return "SIZForPurchase{" +
                "id=" + id +
                ", namesiz='" + namesiz + '\'' +
                ", height='" + height + '\'' +
                ", size='" + size + '\'' +
                ", numAll=" + numAll +
                ", numIssued=" + numIssued +
                ", numPurchase=" + numPurchase +
                '}';
    }
}
