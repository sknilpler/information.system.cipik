package com.information.system.cipik.utils;

import com.information.system.cipik.models.IssuanceItems;
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

public class IssuanceItemsExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    List<IssuanceItems> itemsList;

    public IssuanceItemsExcelExporter(List<IssuanceItems> itemsList) {
        this.itemsList = itemsList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Выданные МЦ");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);

        createCell(row, 0, "№ п/п", style);
        createCell(row, 1, "Наименование", style);
        createCell(row, 2, "№ накладной", style);
        createCell(row, 3, "Дата выдачи", style);
        createCell(row, 4, "Ед. изм.", style);
        createCell(row, 5, "Кол-во", style);
        createCell(row, 6, "Подразделение", style);
        createCell(row, 7, "Кто выдал", style);
        createCell(row, 8, "Кто принял", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else if (value instanceof Double) {
            cell.setCellValue((Double) value);
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
        for (IssuanceItems e: itemsList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, rowCount-1, style);
            createCell(row, columnCount++, e.getItem().getName(), style);
            createCell(row, columnCount++, e.getInvoiceNumber(), style);
            createCell(row, columnCount++, dateFormatter.format(e.getDateIssued()), style);
            createCell(row, columnCount++, e.getItem().getUnit(), style);
            createCell(row, columnCount++, e.getNumber(), style);
            createCell(row, columnCount++, e.getKomplex().getShortName(), style);
            createCell(row, columnCount++, e.getEmployeeGaveOut(), style);
            createCell(row, columnCount++, e.getEmployeeAccepting(), style);
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
