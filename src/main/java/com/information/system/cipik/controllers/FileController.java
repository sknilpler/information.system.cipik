package com.information.system.cipik.controllers;

import com.ibm.icu.text.Transliterator;
import com.information.system.cipik.models.Employee;
import com.information.system.cipik.models.IPMStandard;
import com.information.system.cipik.repo.EmployeeRepository;
import com.information.system.cipik.repo.IPMStandardRepository;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class FileController {

    private String folderPath = Paths.get("").toAbsolutePath().toString()+File.separator+"DBBackupFiles";
  //  private String folderPath = "/home/first/Рабочий стол/ПРОЕКТ информационной системы ЦИП ИК/DBBackupFiles";
    private String dbUserName = "craft";
    private String dbUserPassword = "111";
    private String dbNameList = "cipik";

    public String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    IPMStandardRepository ipmStandardRepository;

    /**
     * Выгрузка SQL дампа БД пользователю
     *
     * @return SQL file
     * @throws IOException
     */
    @GetMapping(value = "admin/download_backup")
    public ResponseEntity<Object> downloadBackupFile() throws IOException {


        Date backupDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String backupDateStr = format.format(backupDate);

        String fileName = "Express_DB_Backup";

        File f1 = new File(folderPath);
        f1.mkdir(); // create folder if not exist

        String saveFileName = fileName + "_" + backupDateStr + ".sql";
        String savePath = folderPath + File.separator + saveFileName;

        String executeCmd = "mysqldump -u " + dbUserName + " -p" + dbUserPassword + " " + dbNameList
                + " > \"" + savePath + "\"";

        Process runtimeProcess = null;
        try {
            String osName = System.getProperty("os.name");

            if (osName.charAt(0) == 'W'){
                runtimeProcess = Runtime.getRuntime().exec(new String[]{"cmd", "/c", executeCmd});  //for Windows
            } else {
                runtimeProcess = Runtime.getRuntime().exec(new String[]{"sh", "-c", executeCmd});   //for Linux
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        int processComplete = 0;
        try {
            assert runtimeProcess != null;
            processComplete = runtimeProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (processComplete == 0) {
            System.out.println("Backup Complete at " + new Date());
        } else {
            System.out.println("Backup Failure with: " + processComplete);
        }

        File dfile = new File(savePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(dfile));
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", dfile.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object>
                responseEntity = ResponseEntity.ok().headers(headers).contentLength(
                dfile.length()).contentType(MediaType.parseMediaType("application/sql")).body(resource);

        if (dfile.delete()) {
            System.out.println(dfile.getName() + " временный файл бэкапа удален!");
        } else {
            System.out.println("Не удалось удалить файл бэкапа " + dfile.getName());
        }

        return responseEntity;
    }

    /**
     * Загрузка на сервер дампа SQL и восстановление БД
     *
     * @param file       файл дампа SQL
     * @param attributes статус выполнения операции
     * @return Сообщение
     * @throws IOException
     */
    @PostMapping(value = "admin/upload_backup")
    public String uploadBackupFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) throws IOException {

        // check if file is empty or not sql
        if (file.isEmpty() || (!getFileExtension(file).equals("sql"))) {
            attributes.addFlashAttribute("message", "Выберите файл формата *.sql для загрузки");
            return "redirect:/admin";
        }

        File convertFile = new File(folderPath + File.separator + file.getOriginalFilename());
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(file.getBytes());
        fout.close();

        String[] executeCmd = new String[]{"mysql", dbNameList, "-u" + dbUserName, "-p" + dbUserPassword, "-e", " source " + folderPath + File.separator + file.getOriginalFilename()};
        System.out.println(folderPath);
        System.out.println(File.separator);
        System.out.println(file.getOriginalFilename());
        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
        int processComplete = 0;
        try {
            processComplete = runtimeProcess.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (processComplete == 0) {
            System.out.println("Успешно восстановлено из SQL : " + file.getOriginalFilename());
        } else {
            System.out.println("Ошибка восстановления");
        }

        // return success response
        attributes.addFlashAttribute("message", "Файл " + file.getOriginalFilename() + " успешно загружен, база данных восстановлена!");

        if (convertFile.delete()) {
            System.out.println(convertFile.getName() + " временный файл бэкапа удален!");
        } else {
            System.out.println("Не удалось удалить файл бэкапа " + convertFile.getName());
        }

        return "redirect:/admin";
    }


    @GetMapping("/userPage/employee-siz/print-personal-card/{id}")
    public void printEmployeePersonalCard (@PathVariable(value = "id") long id, HttpServletResponse response) throws IOException {
        Employee employee = employeeRepository.findById(id).orElseThrow();
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=lichnaya_kartochka_"+toLatinTrans.transliterate(employee.getSurname()+"_"+employee.getName()+"_"+employee.getPatronymic())+".xlsx";
        response.setHeader(headerKey, headerValue);

        String excelFilePath = Paths.get("").toAbsolutePath().toString()+File.separator+"template"+File.separator+"Личная карточка.xlsx";

        try {
            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);

            Cell cellSurname = sheet.getRow(6).getCell(1);
            Cell cellName = sheet.getRow(7).getCell(1);
            Cell cellPatronymic = sheet.getRow(7).getCell(4);
            Cell cellTabnom = sheet.getRow(8).getCell(3);
            Cell cellKomplex = sheet.getRow(9).getCell(3);
            Cell cellPost = sheet.getRow(10).getCell(2);

            Cell cellDateStartWork = sheet.getRow(11).getCell(2);
            Cell cellSex = sheet.getRow(6).getCell(8);
            Cell cellHeight = sheet.getRow(7).getCell(8);
            Cell cellSizeWear = sheet.getRow(9).getCell(8);
            Cell cellSizeShoe = sheet.getRow(10).getCell(8);
            Cell cellSizeHead = sheet.getRow(11).getCell(9);
            Cell cellSizeGasMask = sheet.getRow(12).getCell(9);
            Cell cellSizeRespirator = sheet.getRow(13).getCell(9);
            Cell cellSizeMittens = sheet.getRow(14).getCell(9);
            Cell cellSizeGlove = sheet.getRow(15).getCell(9);

            cellSurname.setCellValue(employee.getSurname());
            cellName.setCellValue(employee.getName());
            cellPatronymic.setCellValue(employee.getPatronymic());
            cellTabnom.setCellValue(employee.getTabNomer());
            cellKomplex.setCellValue(employee.getKomplex().getShortName());
            cellPost.setCellValue(employee.getPost().getPostName());
            DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
            cellDateStartWork.setCellValue(dateFormatter.format(employee.getDataStartWork()));
            cellSex.setCellValue(employee.getSex());
            cellHeight.setCellValue(employee.getHeight());
            cellSizeWear.setCellValue(employee.getClothingSize());
            cellSizeShoe.setCellValue(employee.getShoeSize());
            cellSizeHead.setCellValue(employee.getHeadgearSize());
            cellSizeGasMask.setCellValue(employee.getGasMaskSize());
            cellSizeRespirator.setCellValue(employee.getRespiratorSize());
            cellSizeMittens.setCellValue(employee.getMittensSize());
            cellSizeGlove.setCellValue(employee.getGloveSize());

            int row = 19;
            Row exampleRow = sheet.getRow(19);
            List<IPMStandard> standards = ipmStandardRepository.findAllByPostId(employee.getPost().getId());
            for (IPMStandard s: standards) {
                sheet.getRow(row).setRowStyle(exampleRow.getRowStyle());
                sheet.getRow(row).setHeight(exampleRow.getHeight());
                sheet.addMergedRegion(new CellRangeAddress(
                        row+1, //first row (0-based)
                        row+1, //last row  (0-based)
                        0, //first column (0-based)
                        3  //last column  (0-based)
                ));
                sheet.addMergedRegion(new CellRangeAddress(
                        row+1, //first row (0-based)
                        row+1, //last row  (0-based)
                        4, //first column (0-based)
                        5  //last column  (0-based)
                ));
                sheet.addMergedRegion(new CellRangeAddress(
                        row+1, //first row (0-based)
                        row+1, //last row  (0-based)
                        6, //first column (0-based)
                        7  //last column  (0-based)
                ));
                sheet.addMergedRegion(new CellRangeAddress(
                        row+1, //first row (0-based)
                        row+1, //last row  (0-based)
                        8, //first column (0-based)
                        9  //last column  (0-based)
                ));

                for (int i = 0; i < 10; i++) {
                    sheet.getRow(row).getCell(i).setCellStyle(exampleRow.getCell(i).getCellStyle());
                }
                sheet.getRow(row).getCell(0).setCellValue(s.getIndividualProtectionMeans().getNameSIZ());
                sheet.getRow(row).getCell(6).setCellValue(s.getIndividualProtectionMeans().getEd_izm());
                sheet.getRow(row).getCell(8).setCellValue(s.getIssuanceRate() + " на "+s.getServiceLife() + " мес");
                row++;
            }

            inputStream.close();

            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (IOException | EncryptedDocumentException
                 ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод определения расширения файла (ХХХХХ.txt -> txt)
     *
     * @param file Файл
     * @return Расширение
     */
    private static String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        // если в имени файла есть точка и она не является первым символом в названии файла
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
            return fileName.substring(fileName.lastIndexOf(".") + 1);
            // в противном случае возвращаем заглушку, то есть расширение не найдено
        else return "";
    }
}
