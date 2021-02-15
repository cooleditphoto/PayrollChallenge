package com.muziqiu.payrollapplication.controller;

import com.muziqiu.payrollapplication.service.PayrollReportService;
import com.muziqiu.payrollapplication.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PayrollController {

    @Autowired
    private PayrollReportService payrollReportService;

    @PostMapping("/timereport")
    public RestResponse postReport(@RequestParam("file") MultipartFile file) {
        boolean flag = false;
        try {
            flag = payrollReportService.saveCSV(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (flag) {
            return RestResponse.succuess("upload CSV succeeded!");
        } else {
            return RestResponse.fail("upload CSV failed, because the report id is duplicated");
        }
    }

    @GetMapping("/payrollreport")
    public RestResponse getPayrollReport() {
        return RestResponse.succuess(payrollReportService.getAllPayReport());
    }
}
