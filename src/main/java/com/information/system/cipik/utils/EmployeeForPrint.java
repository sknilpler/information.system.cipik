package com.information.system.cipik.utils;

import com.information.system.cipik.models.Employee;
import com.information.system.cipik.models.IssuedSIZ;

public class EmployeeForPrint {
    private Employee employee;
    private IssuedSIZ issuedSIZ;
    private String staffing;

    public EmployeeForPrint(Employee employee, IssuedSIZ issuedSIZ, String staffing) {
        this.employee = employee;
        this.issuedSIZ = issuedSIZ;
        this.staffing = staffing;
    }

    public EmployeeForPrint() {
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public IssuedSIZ getIssuedSIZ() {
        return issuedSIZ;
    }

    public void setIssuedSIZ(IssuedSIZ issuedSIZ) {
        this.issuedSIZ = issuedSIZ;
    }

    public String getStaffing() {
        return staffing;
    }

    public void setStaffing(String staffing) {
        this.staffing = staffing;
    }
}
