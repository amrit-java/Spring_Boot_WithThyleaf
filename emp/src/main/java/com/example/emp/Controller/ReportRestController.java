package com.example.emp.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.emp.excelfile.ReportService;
@RestController
public class ReportRestController {
	@Autowired
	private ReportService reportService;
	@GetMapping("/excel")
	public void generateExcelReport(HttpServletResponse response) throws IOException {
		
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValu = "attachment;filename=student.xls";
		response.setHeader(headerKey, headerValu);
		
		reportService.generateExcel(response);
		
		
	}

}
