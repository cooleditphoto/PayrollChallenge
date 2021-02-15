package com.muziqiu.payrollapplication.service;

import com.muziqiu.payrollapplication.mapper.TimeReportMapper;
import com.muziqiu.payrollapplication.pojo.PayrollReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PayrollReportServiceTest {

    @Autowired
    private PayrollReportService payrollReportService;
    @Autowired
    private TimeReportMapper timeReportMapper;

    @BeforeEach
    void prepareDatabase() throws IOException {
        timeReportMapper.deleteAll();
        InputStream fileInputStream;
        fileInputStream = new FileInputStream("time-report-42.csv");

        MockMultipartFile multipartFile = new MockMultipartFile("file", "time-report-42.csv",
                "text/plain", fileInputStream);
        payrollReportService.saveCSV(multipartFile);
    }

    @Test
    public void testGetAllPayrollReport() throws IOException {
        PayrollReport payrollReport = payrollReportService.getAllPayReport();
        assertEquals(13, payrollReport.getEmployeeReports().size());

        InputStream fileInputStream = new FileInputStream("time-report-43.csv");

        MockMultipartFile multipartFile = new MockMultipartFile("file", "time-report-43.csv",
                "text/plain", fileInputStream);
        payrollReportService.saveCSV(multipartFile);

        payrollReport = payrollReportService.getAllPayReport();
        assertEquals(16, payrollReport.getEmployeeReports().size());

        assertEquals("$150.00", payrollReport.getEmployeeReports().get(0).getAmountPaid());
        assertEquals("$220.00", payrollReport.getEmployeeReports().get(1).getAmountPaid());
        assertEquals("$150.00", payrollReport.getEmployeeReports().get(2).getAmountPaid());
        assertEquals("$220.00", payrollReport.getEmployeeReports().get(3).getAmountPaid());
        assertEquals("$350.00", payrollReport.getEmployeeReports().get(4).getAmountPaid());
        assertEquals("$120.00", payrollReport.getEmployeeReports().get(5).getAmountPaid());
        assertEquals("$930.00", payrollReport.getEmployeeReports().get(6).getAmountPaid());
        assertEquals("$930.00", payrollReport.getEmployeeReports().get(7).getAmountPaid());
        assertEquals("$90.00", payrollReport.getEmployeeReports().get(8).getAmountPaid());
        assertEquals("$590.00", payrollReport.getEmployeeReports().get(9).getAmountPaid());
        assertEquals("$470.00", payrollReport.getEmployeeReports().get(10).getAmountPaid());
        assertEquals("$150.00", payrollReport.getEmployeeReports().get(11).getAmountPaid());
        assertEquals("$150.00", payrollReport.getEmployeeReports().get(12).getAmountPaid());
        assertEquals("$450.00", payrollReport.getEmployeeReports().get(13).getAmountPaid());
        assertEquals("$150.00", payrollReport.getEmployeeReports().get(14).getAmountPaid());
        assertEquals("$450.00", payrollReport.getEmployeeReports().get(15).getAmountPaid());
    }
}
