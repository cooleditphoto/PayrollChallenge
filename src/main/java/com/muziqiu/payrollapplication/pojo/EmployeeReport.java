package com.muziqiu.payrollapplication.pojo;

public class EmployeeReport {

    Integer employeeId;
    PayPeriod payPeriod;
    String amountPaid;

    public EmployeeReport(Integer employeeId, PayPeriod payPeriod, String amountPaid) {
        this.employeeId = employeeId;
        this.payPeriod = payPeriod;
        this.amountPaid = amountPaid;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public PayPeriod getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(PayPeriod payPeriod) {
        this.payPeriod = payPeriod;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }
}
