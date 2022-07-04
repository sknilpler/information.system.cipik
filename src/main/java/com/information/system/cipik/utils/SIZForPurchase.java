package com.information.system.cipik.utils;

public class SIZForPurchase {
    private Long id;
    private String namesiz;
    private String height;
    private String size;
    private int numAll;
    private int numIssued;
    private int numPurchase;
    private String fio;
    private String post;
    private String komplex;

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

    public SIZForPurchase(Long id, String namesiz, String height, String size, int numAll, int numIssued, String fio, String post, String komplex) {
        this.id = id;
        this.namesiz = namesiz;
        this.height = height;
        this.size = size;
        this.numAll = numAll;
        this.numIssued = numIssued;
        this.numPurchase = numAll-numIssued;
        this.fio = fio;
        this.post = post;
        this.komplex = komplex;
    }

    public SIZForPurchase(Long id, String namesiz, String height, String size, int num, String fio, String post, String komplex) {
        this.id = id;
        this.namesiz = namesiz;
        this.height = height;
        this.size = size;
        this.numAll = num;
        this.fio = fio;
        this.post = post;
        this.komplex = komplex;
    }


    public SIZForPurchase(Long id, String namesiz, String height, String size, int num) {
        this.id = id;
        this.namesiz = namesiz;
        this.height = height;
        this.size = size;
        this.numAll = num;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getKomplex() {
        return komplex;
    }

    public void setKomplex(String komplex) {
        this.komplex = komplex;
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
                ", fio='" + fio + '\'' +
                ", post='" + post + '\'' +
                ", komplex='" + komplex + '\'' +
                '}';
    }
}
