package com.information.system.cipik.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class EmployeeStaffingExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    List<EmployeeForPrint> listEmployees;

    public EmployeeStaffingExcelExporter(List<EmployeeForPrint> listEmployees) {
        this.listEmployees = listEmployees;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Сотрудники");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        font.setFontName("Times New Roman");
        style.setFont(font);

        createCell(row, 0, "№ п/п", style);
        createCell(row, 1, "Подразделение", style);
        createCell(row, 2, "Должность", style);
        createCell(row, 3, "Фамилия", style);
        createCell(row, 4, "Имя", style);
        createCell(row, 5, "Отчество", style);
        createCell(row, 6, "Укомплектованность", style);
        createCell(row, 7, "СИЗ с ближайшей датой окончания носки", style);
        createCell(row, 8, "Дата окончания носки", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        font.setFontName("Times New Roman");
        style.setFont(font);
        DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        for (EmployeeForPrint e: listEmployees) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, rowCount-1, style);
            createCell(row, columnCount++, e.getEmployee().getKomplex().getShortName(), style);
            createCell(row, columnCount++, e.getEmployee().getPost().getPostName(), style);
            createCell(row, columnCount++, e.getEmployee().getSurname(), style);
            createCell(row, columnCount++, e.getEmployee().getName(), style);
            createCell(row, columnCount++, e.getEmployee().getPatronymic(), style);
            createCell(row, columnCount++, (e.getStaffing()==null ? "0%":e.getStaffing()+"%"), style);
            if (e.getIssuedSIZ() != null) {
                createCell(row, columnCount++, e.getIssuedSIZ().getSiz().getNameSIZ(), style);
                createCell(row, columnCount++, dateFormatter.format(e.getIssuedSIZ().getDateEndWear()), style);
            } else {
                createCell(row, columnCount++, "", style);
                createCell(row, columnCount++, "", style);
            }
        }

    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
