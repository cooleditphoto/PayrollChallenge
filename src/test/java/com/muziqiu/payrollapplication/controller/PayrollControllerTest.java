package com.muziqiu.payrollapplication.controller;

import com.muziqiu.payrollapplication.mapper.TimeReportMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class PayrollControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TimeReportMapper timeReportMapper;

    @BeforeEach
    void cleanDatabase() {
        timeReportMapper.deleteAll();
    }

    @Test
    public void testUploadCSV() throws Exception {
        InputStream fileInputStream;
        fileInputStream = new FileInputStream("time-report-42.csv");

        MockMultipartFile multipartFile = new MockMultipartFile("file", "time-report-42.csv",
                "text/plain", fileInputStream);
        this.mockMvc.perform(multipart("/timereport").file(multipartFile))
                .andExpect(status().isOk()).andExpect(content().string(containsString("upload CSV succeeded!")));

        this.mockMvc.perform(multipart("/timereport").file(multipartFile))
                .andExpect(status().isOk()).andExpect(content().string(containsString("upload CSV failed")));

        assertEquals(31, timeReportMapper.countByReportId(42));
    }
}
