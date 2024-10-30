package com.example.scholar.service.impl;

import com.example.scholar.config.PathConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

@Service("FileService")
public class FileService {
    public String updateFile(MultipartFile multipartFile){
        if (multipartFile.isEmpty()) {
            return null;
        }
        String fileName = multipartFile.getOriginalFilename() + System.currentTimeMillis();
        //文件路径
        String filePath = PathConfig.path + FileSystems.getDefault().getSeparator() + "img"+ FileSystems.getDefault().getSeparator() +"avatar";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        File dest = new File(filePath + FileSystems.getDefault().getSeparator() + fileName);
        //存储到数据库里的相对文件地址
        String storePath = "/img/avatar/" + fileName;
        try {
            multipartFile.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return storePath;
    }
}
