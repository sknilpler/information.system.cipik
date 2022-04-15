package com.information.system.cipik.utils;

public class StatisticsForListSIZ {

    /**
     * <b>Кол-во всего выданного СИЗ</b>
     */
    private int numberOfAllIssuedSIZ;
    /**
     * <b>Кол-во СИЗ с истекшим сроком носки</b>
     */
    private int numberOfEndIssuedSIZ;
    /**
     * <b>Кол-во СИЗ с истекающим сроком носки в следующем году</b>
     */
    private int numberOfEndIssuedSIZNextYear;
    /**
     * <b>Кол-во СИЗ с истекающим сроком носки в этом году</b>
     */
    private int numberOfEndIssuedSIZThisYear;

    public StatisticsForListSIZ() {
    }

    /**
     * <b>Класс статистика выданного СИЗ</b>
     *
     * @param numberOfAllIssuedSIZ         Кол-во всего выданного СИЗ
     * @param numberOfEndIssuedSIZ         Кол-во СИЗ с истекшим сроком носки
     * @param numberOfEndIssuedSIZNextYear Кол-во СИЗ с истекающим сроком носки в следующем году
     * @param numberOfEndIssuedSIZThisYear Кол-во СИЗ с истекающим сроком носки в этом году
     */
    public StatisticsForListSIZ(int numberOfAllIssuedSIZ, int numberOfEndIssuedSIZ, int numberOfEndIssuedSIZNextYear, int numberOfEndIssuedSIZThisYear) {
        this.numberOfAllIssuedSIZ = numberOfAllIssuedSIZ;
        this.numberOfEndIssuedSIZ = numberOfEndIssuedSIZ;
        this.numberOfEndIssuedSIZNextYear = numberOfEndIssuedSIZNextYear;
        this.numberOfEndIssuedSIZThisYear = numberOfEndIssuedSIZThisYear;
    }

    public int getNumberOfAllIssuedSIZ() {
        return numberOfAllIssuedSIZ;
    }

    public void setNumberOfAllIssuedSIZ(int numberOfAllIssuedSIZ) {
        this.numberOfAllIssuedSIZ = numberOfAllIssuedSIZ;
    }

    public int getNumberOfEndIssuedSIZ() {
        return numberOfEndIssuedSIZ;
    }

    public void setNumberOfEndIssuedSIZ(int numberOfEndIssuedSIZ) {
        this.numberOfEndIssuedSIZ = numberOfEndIssuedSIZ;
    }

    public int getNumberOfEndIssuedSIZNextYear() {
        return numberOfEndIssuedSIZNextYear;
    }

    public void setNumberOfEndIssuedSIZNextYear(int numberOfEndIssuedSIZNextYear) {
        this.numberOfEndIssuedSIZNextYear = numberOfEndIssuedSIZNextYear;
    }

    public int getNumberOfEndIssuedSIZThisYear() {
        return numberOfEndIssuedSIZThisYear;
    }

    public void setNumberOfEndIssuedSIZThisYear(int numberOfEndIssuedSIZThisYear) {
        this.numberOfEndIssuedSIZThisYear = numberOfEndIssuedSIZThisYear;
    }
}
