package com.information.system.cipik.utils;

import com.information.system.cipik.models.Employee;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class EmployeeExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    List<Employee> listEmployees;

    public EmployeeExcelExporter(List<Employee> listEmployees) {
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
        style.setFont(font);

        createCell(row, 0, "№ п/п", style);
        createCell(row, 1, "Подразделение", style);
        createCell(row, 2, "Должность", style);
        createCell(row, 3, "Фамилия", style);
        createCell(row, 4, "Имя", style);
        createCell(row, 5, "Отчество", style);
        createCell(row, 6, "Рост", style);
        createCell(row, 7, "Размер одежды", style);
        createCell(row, 8, "Размер головного убора", style);
        createCell(row, 9, "Размер обуви", style);

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
        style.setFont(font);

        for (Employee e: listEmployees) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, rowCount, style);
            createCell(row, columnCount++, e.getOtdel().getName(), style);
            createCell(row, columnCount++, e.getPost().getPostName(), style);
            createCell(row, columnCount++, e.getSurname(), style);
            createCell(row, columnCount++, e.getName(), style);
            createCell(row, columnCount++, e.getPatronymic(), style);
            createCell(row, columnCount++, e.getHeight(), style);
            createCell(row, columnCount++, e.getClothingSize(), style);
            createCell(row, columnCount++, e.getHeadgearSize(), style);
            createCell(row, columnCount++, e.getShoeSize(), style);
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
