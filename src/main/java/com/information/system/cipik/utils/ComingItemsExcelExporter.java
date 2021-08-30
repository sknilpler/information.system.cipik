package com.information.system.cipik.utils;

import com.information.system.cipik.models.Coming;
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

public class ComingItemsExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    List<Coming> comingList;

    public ComingItemsExcelExporter(List<Coming> comingList) {
        this.comingList = comingList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Приходы");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);

        createCell(row, 0, "№ п/п", style);
        createCell(row, 1, "Счет", style);
        createCell(row, 2, "Наименование", style);
        createCell(row, 3, "Номенклатурный №", style);
        createCell(row, 4, "Цена", style);
        createCell(row, 5, "Ед. изм.", style);
        createCell(row, 6, "Кол-во", style);
        createCell(row, 7, "Дата прихода", style);

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
        DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        for (Coming e: comingList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, rowCount-1, style);
            createCell(row, columnCount++, e.getBill(), style);
            createCell(row, columnCount++, e.getName(), style);
            createCell(row, columnCount++, e.getNomenclature(), style);
            createCell(row, columnCount++, e.getPrice(), style);
            createCell(row, columnCount++, e.getUnit(), style);
            createCell(row, columnCount++, e.getNumber(), style);
            createCell(row, columnCount++, dateFormatter.format(e.getDateOfReceive()), style);
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
