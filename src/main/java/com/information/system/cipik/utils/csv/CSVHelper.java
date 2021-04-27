package com.information.system.cipik.utils.csv;

import com.information.system.cipik.models.Employee;
import com.information.system.cipik.models.Komplex;
import com.information.system.cipik.models.Post;
import com.information.system.cipik.repo.KomplexRepository;
import com.information.system.cipik.repo.PostRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "Komplex", "Post", "S","N","P","Height","Size","HSize" };

    private static PostRepository postRepository;

    private static KomplexRepository komplexRepository;

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    @Autowired
    public CSVHelper(PostRepository postRepository,KomplexRepository komplexRepository){
        CSVHelper.postRepository = postRepository;
        CSVHelper.komplexRepository = komplexRepository;
    }

    public static Iterable<Employee> csvToEmployees(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Employee> employees = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            System.out.println("size csv rec: "+csvParser.getRecords().size());
            for (CSVRecord csvRecord : csvRecords) {
                Komplex k = komplexRepository.findByShortName(csvRecord.get("Komplex"));
                Post p = postRepository.findByPostName(csvRecord.get("Post"));
                String surname = csvRecord.get("S");
                String sex = "Мужской";
                if (surname.substring(surname.length()-1).equals("а")){
                    sex = "Женский";
                    System.out.println("Фамилия: "+surname+" женская");
                }
                Employee employee = new Employee(
                        surname,
                        csvRecord.get("N"),
                        csvRecord.get("P"),
                        sex,
                        csvRecord.get("Height"),
                        csvRecord.get("Size"),
                        csvRecord.get("HSize"),
                        p,
                        k
                );

                employees.add(employee);
            }

            return employees;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
