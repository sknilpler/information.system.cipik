package com.information.system.cipik.controllers;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class FileController {

    private String folderPath = "/home/first/Рабочий стол/ПРОЕКТ информационной системы ЦИП ИК/DBBackupFiles";
    private String dbUserName = "craft";
    private String dbUserPassword = "111";
    private String dbNameList = "cipik";

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
            runtimeProcess = Runtime.getRuntime().exec(new String[]{"sh", "-c", executeCmd});
        } catch (IOException e) {
            e.printStackTrace();
        }
        int processComplete = 0;
        try {
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

        File convertFile = new File(folderPath + "/" + file.getOriginalFilename());
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(file.getBytes());
        fout.close();

        String[] executeCmd = new String[]{"mysql", dbNameList, "-u" + dbUserName, "-p" + dbUserPassword, "-e", " source " + folderPath + File.separator + file.getOriginalFilename()};

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
