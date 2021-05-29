package com.information.system.cipik.utils;

import com.information.system.cipik.repo.SIZRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SIZToKomplexActExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<SIZForKomplex> objectList;

    @Autowired
    SIZRepository sizRepository;

    public SIZToKomplexActExcelExporter(List<SIZForKomplex> objectList) {
        this.objectList = objectList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Акт приема передачи СИЗ в подразделение");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        font.setFontName("Times New Roman");
        style.setFont(font);

        createCell(row, 0, "№ п/п", style);
        createCell(row, 1, "Номенклатурный номер", style);
        createCell(row, 2, "Наименование", style);
        createCell(row, 3, "Кол-во", style);
        createCell(row, 4, "Ед. изм.", style);
        createCell(row, 5, "Примечание", style);
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
        for (SIZForKomplex o: objectList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, rowCount-1, style);
            createCell(row, columnCount++, o.getNomenclatureNumber(), style);
            createCell(row, columnCount++, o.getNamesiz(), style);
            createCell(row, columnCount++, o.getNumber(), style);
            createCell(row, columnCount++, sizRepository.findById(o.getId()).orElseThrow().getEd_izm(), style);
            createCell(row, columnCount++, "", style);
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


