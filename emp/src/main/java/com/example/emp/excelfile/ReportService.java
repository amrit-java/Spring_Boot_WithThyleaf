package com.example.emp.excelfile;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.emp.domain.Student;
import com.example.emp.repository.StudentRepository;

@Service
public class ReportService {
	
	 @Autowired
	    private StudentRepository repo;
	 
	 public void generateExcel(HttpServletResponse response) throws IOException {
	    	
		 List<Student> liststudent = repo.findAll();
		 
		 HSSFWorkbook workbook = new HSSFWorkbook();
		 
		HSSFSheet sheet= workbook.createSheet("StudentList");
		
		HSSFRow row= sheet.createRow(0);
		
		row.createCell(0).setCellValue("id");
		row.createCell(1).setCellValue("studentname");
		row.createCell(2).setCellValue("course");
		
		int dataRowIndex =1;
		
		for(Student student : liststudent) {
			HSSFRow dataRow= sheet.createRow(dataRowIndex);
			dataRow.createCell(0).setCellValue(student.getId());
			dataRow.createCell(1).setCellValue(student.getStudentname());
			dataRow.createCell(2).setCellValue(student.getFee());
			
			dataRowIndex++;
		}
		
		ServletOutputStream ops= response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
		 
	    }

}
