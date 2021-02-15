CREATE TABLE IF NOT EXISTS `time_report`(
    `id` INT UNSIGNED AUTO_INCREMENT,
    `date` DATE NOT NULL,
    `hours_worked` decimal(10,2) NOT NULL,
    `job_group` VARCHAR(40) NOT NULL,
    `employee_id` INT NOT NULL,
    `report_id` INT NOT NULL,
    PRIMARY KEY ( `id` )
 );

INSERT INTO time_report (`date`, hours_worked,job_group, employee_id, report_id) VALUES
('2020-01-04', 10, 'A', 1, 43),
('2020-01-14', 5, 'A', 1, 43),
('2020-01-20', 3, 'B', 2, 43),
('2020-01-20', 4, 'A', 1, 43);