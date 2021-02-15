package com.muziqiu.payrollapplication.service;

import com.muziqiu.payrollapplication.mapper.TimeReportMapper;
import com.muziqiu.payrollapplication.model.TimeReport;
import com.muziqiu.payrollapplication.pojo.EmployeeReport;
import com.muziqiu.payrollapplication.pojo.PayPeriod;
import com.muziqiu.payrollapplication.pojo.PayrollReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class PayrollReportService {

    @Autowired
    private TimeReportMapper timeReportMapper;

    public boolean saveCSV(MultipartFile reportFile) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        int reportId = Integer.parseInt(Objects.requireNonNull(reportFile.getOriginalFilename()).replace(".csv", "").split("-")[2]);
        if (isDuplicateReportId(reportId)) {
            return false;
        }
        InputStream fileInputStream;
        BufferedReader bufferedReader;
        fileInputStream = reportFile.getInputStream();
        bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        bufferedReader
                .lines()
                .filter(s -> !s.contains("job"))
                .map(s -> {
                    String[] splitStrings = s.split(",");
                    TimeReport report = new TimeReport();
                    LocalDate date = LocalDate.parse(splitStrings[0], DateTimeFormatter.ofPattern("d/M/yyyy"));
                    report.setDate(Date.from(date.atTime(12,0).atZone(ZoneId.systemDefault()).toInstant()));
                    report.setHoursWorked(Double.parseDouble(splitStrings[1]));
                    report.setEmployeeId(Integer.parseInt(splitStrings[2]));
                    report.setJobGroup(splitStrings[3]);
                    report.setReportId(reportId);
                    return report;
                }).forEach(r -> timeReportMapper.insert(r));

        return true;
    }

    private boolean isDuplicateReportId(Integer reportId) {
        long count = timeReportMapper.countByReportId(reportId);
        return count != 0;
    }

    public PayrollReport getAllPayReport() {
        List<TimeReport> payrollreportList = timeReportMapper.selectOrderByEmployeeIdandDate();
        TreeMap<Integer, List<TimeReport>> reportsByEmployeeId = payrollreportList.stream()
                .collect(groupingBy(TimeReport::getEmployeeId, TreeMap::new, toList()));

        List<EmployeeReport> employeeReports = new LinkedList<>();
        for (Map.Entry<Integer, List<TimeReport>> entry : reportsByEmployeeId.entrySet()) {
            entry.getValue().sort(Comparator.comparing(TimeReport::getDate));
            List<TimeReport> reportsSortByDate = entry.getValue();

            String currentPayperiodstart = "";
            String currentPayperiodend = "";
            double amountPaid = 0;
            for (TimeReport payrollreport : reportsSortByDate) {
                Date date = payrollreport.getDate();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(date);
                String[] datestrings = strDate.split("-");
                int year = Integer.parseInt(datestrings[0]);
                int month = Integer.parseInt(datestrings[1]);
                String monthString = datestrings[1];
                int day = Integer.parseInt(datestrings[2]);

                //specify the first pay period
                if ("".equals(currentPayperiodstart)) {
                    if (day <= 15) {
                        currentPayperiodstart = year + "-" + monthString + "-" + "01";
                        currentPayperiodend = year + "-" + monthString + "-" + "15";
                    } else {
                        currentPayperiodstart = year + "-" + monthString + "-" + "16";
                        YearMonth yearMonthObject = YearMonth.of(year, month);
                        int daysInMonth = yearMonthObject.lengthOfMonth();
                        currentPayperiodend = year + "-" + monthString + "-" + daysInMonth;
                    }
                }

                LocalDate currentPayperiodendDateStart = LocalDate.parse(currentPayperiodstart, DateTimeFormatter.ISO_DATE);
                LocalDate currentPayperiodendDateEnd = LocalDate.parse(currentPayperiodend, DateTimeFormatter.ISO_DATE);

                //a new pay period
                if (date.after(Date.from(currentPayperiodendDateEnd.atTime(23,59).atZone(ZoneId.systemDefault()).toInstant()))) {
                    PayPeriod payPeriod = new PayPeriod(currentPayperiodstart, currentPayperiodend);
                    String amount = "$" + String.format("%.2f", amountPaid);
                    EmployeeReport employeeReport = new EmployeeReport(payrollreport.getEmployeeId(), payPeriod, amount);
                    employeeReports.add(employeeReport);

                    if (day <= 15) {
                        currentPayperiodstart = year + "-" + monthString + "-" + "01";
                        currentPayperiodend = year + "-" + monthString + "-" + "15";
                    } else {
                        currentPayperiodstart = year + "-" + monthString + "-" + "16";
                        YearMonth yearMonthObject = YearMonth.of(year, month);
                        int daysInMonth = yearMonthObject.lengthOfMonth();
                        currentPayperiodend = year + "-" + monthString + "-" + daysInMonth;
                    }

                    amountPaid=0;
                    amountPaid += payperday(payrollreport.getHoursWorked(), payrollreport.getJobGroup());
                }
                //current pay period
                if ((date.after(Date.from(currentPayperiodendDateStart.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                        || date.equals(Date.from(currentPayperiodendDateStart.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                ) && (date.before(Date.from(currentPayperiodendDateEnd.atTime(23,59).atZone(ZoneId.systemDefault()).toInstant()))
                        || date.equals(Date.from(currentPayperiodendDateEnd.atTime(23,59).atZone(ZoneId.systemDefault()).toInstant())))) {
                    amountPaid += payperday(payrollreport.getHoursWorked(), payrollreport.getJobGroup());
                }
                //the last pay period of this employee
                if (date.equals(reportsSortByDate.get(reportsSortByDate.size() - 1).getDate())) {
                    PayPeriod payPeriod = new PayPeriod(currentPayperiodstart, currentPayperiodend);
                    String amount = "$" + String.format("%.2f", amountPaid);
                    EmployeeReport employeeReport = new EmployeeReport(payrollreport.getEmployeeId(), payPeriod, amount);
                    employeeReports.add(employeeReport);
                }


            }
        }

        return new PayrollReport(employeeReports);
    }

    private double payperday(Double hoursWorked, String jobGroup) {
        if (jobGroup.equals("A")) {
            return 20 * hoursWorked;
        } else {
            return 30 * hoursWorked;
        }
    }
}
