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
        String fileName = multipartFile.getOriginalFilename();
        if (fileName != null) {
            int dotIndex = fileName.lastIndexOf('.');
            String name = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
            String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);
            fileName = name + "_" + System.currentTimeMillis() + extension;

        }
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

    public void deleteFile(String path){
        String filePath = PathConfig.path + path;
        System.out.println(filePath);

        if(path!=null&&!"/img/avatar/default.jpg".equals(path)&&!"/img/avatar/default.png".equals(path)){

            File userPic = new File(filePath);
            if (!userPic.exists()) {
                System.out.println("File does not exist: " + userPic.getAbsolutePath());
                return;
            }
            if (!userPic.delete()) {
                System.out.println(path + ":\n" + "头像不存在或删除失败:SingerController-deleteSinger");
            }

        }
    }
}
