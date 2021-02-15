package com.muziqiu.payrollapplication.pojo;

import java.util.List;

public class PayrollReport {
    List<EmployeeReport> employeeReports;

    public PayrollReport(List<EmployeeReport> employeeReports) {
        this.employeeReports = employeeReports;
    }

    public List<EmployeeReport> getEmployeeReports() {
        return employeeReports;
    }

    public void setEmployeeReports(List<EmployeeReport> employeeReports) {
        this.employeeReports = employeeReports;
    }
}
