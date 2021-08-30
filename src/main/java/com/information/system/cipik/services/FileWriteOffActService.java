package com.information.system.cipik.services;

import com.information.system.cipik.models.FileWriteOffAct;
import com.information.system.cipik.repo.FileWriteOffActRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileWriteOffActService {
    @Autowired
    private FileWriteOffActRepository fileWriteOffActRepository;

    public FileWriteOffAct store(MultipartFile file) throws IOException{
       String fileName = StringUtils.cleanPath(file.getOriginalFilename());
       FileWriteOffAct fileWriteOffAct = new FileWriteOffAct(fileName, file.getContentType(),file.getBytes());

       return fileWriteOffActRepository.save(fileWriteOffAct);
    }

    public FileWriteOffAct getFile(Long id){
        return fileWriteOffActRepository.findById(id).orElse(null);
    }

    public void deleteFile(Long id){
        fileWriteOffActRepository.deleteById(id);
    }

}
