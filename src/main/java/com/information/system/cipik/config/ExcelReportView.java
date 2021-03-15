package com.information.system.cipik.config;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ExcelReportView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

//        response.setHeader("Content-Disposition", "attachment;filename=\"student.xls\"");
//        List<Student> studentList = (List<Student>) model.get("studentList");
//        Sheet sheet = workbook.createSheet("Student Data");
//        Row header = sheet.createRow(0);
//        header.createCell(0).setCellValue("Student ID");
//        header.createCell(1).setCellValue("Student Name");
//        header.createCell(2).setCellValue("Student Mobile");
//
//        int rowNum = 1;
//        for(Student student:studentList){
//            Row row = sheet.createRow(rowNum++);
//            row.createCell(0).setCellValue(student.getStudentId());
//            row.createCell(1).setCellValue(student.getStudentName());
//            row.createCell(2).setCellValue(student.getStudentMobileNum());
//        }
    }
}
