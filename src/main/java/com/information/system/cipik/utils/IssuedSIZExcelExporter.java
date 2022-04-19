package com.information.system.cipik.utils;

import com.information.system.cipik.models.IssuedSIZ;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class IssuedSIZExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    List<IssuedSIZ> sizList;

    public IssuedSIZExcelExporter(List<IssuedSIZ> sizList) {
        this.sizList = sizList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Список выданного СИЗ");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);

        createCell(row, 0, "№ п/п", style);
        createCell(row, 1, "Наименование", style);
        createCell(row, 2, "Тип СИЗ", style);
        createCell(row, 3, "ФИО сотрудника", style);
        createCell(row, 4, "Должность", style);
        createCell(row, 5, "Подразделение", style);
        createCell(row, 6, "Дата выдачи", style);
        createCell(row, 7, "Дата окончания носки", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (value instanceof Timestamp) {
            Timestamp ts = (Timestamp) value;
            Date date = new Date(ts.getTime());
            cell.setCellValue(new SimpleDateFormat("dd.MM.yyyy").format(date));
        }else {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (IssuedSIZ i: sizList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, rowCount-1, style);
            createCell(row, columnCount++, i.getSiz().getNameSIZ(), style);
            createCell(row, columnCount++, i.getSiz().getTypeIPM(), style);
            createCell(row, columnCount++, i.getEmployee().getFIO(), style);
            createCell(row, columnCount++, i.getEmployee().getPost().getPostName(), style);
            createCell(row, columnCount++, i.getEmployee().getKomplex().getShortName(), style);
            createCell(row, columnCount++, i.getDateIssued(), style);
            createCell(row, columnCount++, i.getDateEndWear(), style);
        }

    }

    public void export(HttpServletResponse response) throws IOException {
        System.out.println("start creating xls....");
        long startTime = System.currentTimeMillis();
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        long endTime = System.currentTimeMillis();
        System.out.println("end creating xls!");
        System.out.println((endTime - startTime) / 1000);
        outputStream.close();

    }
}
