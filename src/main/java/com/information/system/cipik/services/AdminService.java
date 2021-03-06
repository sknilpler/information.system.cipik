package com.information.system.cipik.services;

import com.information.system.cipik.utils.EncodingAES;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Objects;

@Service
public class AdminService implements EncodingAES {
    /**
     * Путь к файлу с настройками БД
     */
    public static final String settingsDBFile = "settings" + File.separator + "db.conf";


    /**
     * Создает по указанному пути файл если отсутствует
     *
     * @param filename путь к файлу
     */
    public void createFile(String filename) {
        String[] s = new String[3];
        File location = new File(filename);
        location.getParentFile().mkdirs(); //если не существует то создать
        try {
            location.createNewFile();
            new FileOutputStream(filename, true).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Функция сохранения настроек БД
     *
     * @param username имя пользователя БД
     * @param password пароль к БД
     * @param dbname   название самой БД
     * @return true - удалось сохранить, false - не удалось сохранить
     */
    public boolean saveDBSettings(String username, String password, String dbname) {
        createFile(settingsDBFile);
        try {
            FileWriter wr = new FileWriter(settingsDBFile, false);
            wr.write(Objects.requireNonNull(encrypt(username)));
            wr.append('\n');
            wr.write(Objects.requireNonNull(encrypt(password)));
            wr.append('\n');
            wr.write(Objects.requireNonNull(encrypt(dbname)));
            wr.append('\n');
            wr.flush();
            wr.close();
            return true;
        } catch (IOException e) {
            System.out.println("Не удалось сохранить настройки БД");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Получение данных аутентификации к БД из файла настроек
     *
     * @return массив строк с данными
     */
    public String[] getDBSettings() {
        String[] s = new String[3];
        createFile(settingsDBFile);
        try (FileReader fr = new FileReader(settingsDBFile)) {
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader readerS = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = readerS.readLine();
            int i = 0;
            while (line != null) {
                s[i] = decrypt(line);
                i++;
                // считываем остальные строки в цикле
                line = readerS.readLine();
            }
            readerS.close();
        } catch (IOException e) {
            System.out.println("Не удалось открыть файл с настройками БД");
            e.printStackTrace();
        }
        return s;
    }


}
