package com.muziqiu.payrollapplication.mapper;

import com.muziqiu.payrollapplication.model.TimeReport;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper

public interface TimeReportMapper {
    @Select("SELECT COUNT(*) FROM time_report WHERE report_id = #{reportId}")
    long countByReportId(Integer reportId);

    @Insert("INSERT INTO time_report(date, hours_worked,job_group, employee_id, report_id) " +
            " VALUES (#{date}, #{hoursWorked}, #{jobGroup}, #{employeeId}, #{reportId})")
    int insert(TimeReport record);

    @Select("SELECT * FROM time_report order by employee_id, date")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "date", column = "date"),
            @Result(property = "hoursWorked", column = "hours_worked"),
            @Result(property = "jobGroup", column = "job_group"),
            @Result(property = "employeeId", column = "employee_id"),
            @Result(property = "reportId", column = "report_id")
    })
    List<TimeReport> selectOrderByEmployeeIdandDate();

    @Delete("truncate table time_report")
    void deleteAll();
}