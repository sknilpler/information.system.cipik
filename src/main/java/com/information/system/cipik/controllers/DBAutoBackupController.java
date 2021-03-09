package com.information.system.cipik.controllers;

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

    @Scheduled(cron = "0 0 0/24 * * ?")
    public void scheduleDbBackup() {
        String dbUserName = "craft";
        String dbUserPassword = "111";
        String dbNameList = "cipik";

        System.out.println("Backup Started at " + new Date());

        Date backupDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String backupDateStr = format.format(backupDate);

        String fileName = "Daily_DB_Backup"; // default file name
        String folderPath = "/home/first/Рабочий стол/ПРОЕКТ информационной системы ЦИП ИК/DBBackupFiles";
        File f1 = new File(folderPath);
        f1.mkdir(); // create folder if not exist

        String saveFileName = fileName + "_" + backupDateStr + ".sql";
        String savePath = folderPath + File.separator + saveFileName;

        String executeCmd = "mysqldump -u " + dbUserName + " -p" + dbUserPassword + " " + dbNameList
                + " | gzip > \"" + savePath + ".gz\"";

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
    }

}

