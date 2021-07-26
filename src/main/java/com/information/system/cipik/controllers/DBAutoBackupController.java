package com.information.system.cipik.controllers;

import com.information.system.cipik.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Configuration
@EnableScheduling
public class DBAutoBackupController {

    @Autowired
    AdminService adminService;


//    Графический синтаксис cron для Quarz
//+-------------------- second (0 - 59)
//|  +----------------- minute (0 - 59)
//|  |  +-------------- hour (0 - 23)
//|  |  |  +----------- day of month (1 - 31)
//|  |  |  |  +-------- month (1 - 12)
//|  |  |  |  |  +----- day of week (0 - 6) (Sunday=0 or 7)
//|  |  |  |  |  |  +-- year [optional]
//|  |  |  |  |  |  |
//*  *  *  *  *  *  * command to be executed

    @Scheduled(cron = "0 0 0/12 * * ?")
    public void scheduleDbBackup() {

        String dbUserName = adminService.getDBSettings()[0];;
        String dbUserPassword = adminService.getDBSettings()[1];
        String dbNameList = adminService.getDBSettings()[2];


        System.out.println("Backup Started at " + new Date());

        Date backupDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String backupDateStr = format.format(backupDate);

        String fileName = "Daily_DB_Backup"; // default file name
        String folderPath = adminService.getPaths()[1]+File.separator+"DBBackupFiles";
       // String folderPath = Paths.get("").toAbsolutePath().toString()+File.separator+"DBBackupFiles";
        File f1 = new File(folderPath);
        f1.mkdir(); // create folder if not exist

        String saveFileName = fileName + "_" + backupDateStr + ".sql";
        String savePath = folderPath + File.separator + saveFileName;

        String executeCmd = "mysqldump -u " + dbUserName + " -p" + dbUserPassword + " " + dbNameList
                + " | gzip > \"" + savePath + ".gz\"";

        Process runtimeProcess = null;
        try {
            String osName = System.getProperty("os.name");
            if (osName.charAt(0) == 'W') {
                String pathToMysql = adminService.getPaths()[0];
               // String pathToMysql = "C:" + File.separator + "OpenServer" + File.separator + "modules" + File.separator + "database" + File.separator + "MySQL-8.0" + File.separator + "bin";
                runtimeProcess = Runtime.getRuntime().exec(new String[]{"cmd", "/c", pathToMysql + File.separator + executeCmd});//for Windows
            }
            else{
                runtimeProcess = Runtime.getRuntime().exec(new String[]{"sh", "-c", executeCmd}); //for Linux
            }
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
    }

}

