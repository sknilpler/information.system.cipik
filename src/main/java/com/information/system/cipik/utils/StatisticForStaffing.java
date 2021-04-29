package com.information.system.cipik.utils;

public class StatisticForStaffing {
    private int numberOfEmployee;
    private int numberOfFullStaffing;
    private int numberOfEndIssuedSIZ;

    public StatisticForStaffing(int numberOfEmployee, int numberOfFullStaffing, int numberOfEndIssuedSIZ) {
        this.numberOfEmployee = numberOfEmployee;
        this.numberOfFullStaffing = numberOfFullStaffing;
        this.numberOfEndIssuedSIZ = numberOfEndIssuedSIZ;
    }

    public StatisticForStaffing() {
    }

    public int getNumberOfEmployee() {
        return numberOfEmployee;
    }

    public void setNumberOfEmployee(int numberOfEmployee) {
        this.numberOfEmployee = numberOfEmployee;
    }

    public int getNumberOfFullStaffing() {
        return numberOfFullStaffing;
    }

    public void setNumberOfFullStaffing(int numberOfFullStaffing) {
        this.numberOfFullStaffing = numberOfFullStaffing;
    }

    public int getNumberOfEndIssuedSIZ() {
        return numberOfEndIssuedSIZ;
    }

    public void setNumberOfEndIssuedSIZ(int numberOfEndIssuedSIZ) {
        this.numberOfEndIssuedSIZ = numberOfEndIssuedSIZ;
    }
}
