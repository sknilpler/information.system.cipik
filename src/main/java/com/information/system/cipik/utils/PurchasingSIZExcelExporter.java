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
import java.util.List;

public class PurchasingSIZExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    List<SIZForPurchase> listToPrint;

    public PurchasingSIZExcelExporter(List<SIZForPurchase> listToPrint) {
        this.listToPrint = listToPrint;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("СИЗ");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        font.setFontName("Times New Roman");
        style.setFont(font);

        createCell(row, 0, "№ п/п", style);
        createCell(row, 1, "Номенклатурный №", style);
        createCell(row, 2, "Наименование СИЗ", style);
        createCell(row, 3, "Рост", style);
        createCell(row, 4, "Размер", style);
        createCell(row, 5, "Кол-во", style);

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

        for (SIZForPurchase e: listToPrint) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, rowCount-1, style);
            createCell(row, columnCount++, e.getNomenclatureNumber(), style);
            createCell(row, columnCount++, e.getNamesiz(), style);
            createCell(row, columnCount++, e.getHeight(), style);
            createCell(row, columnCount++, e.getSize(), style);
            createCell(row, columnCount++, e.getNumPurchase(), style);
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
