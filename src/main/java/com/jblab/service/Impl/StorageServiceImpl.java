package com.jblab.service.Impl;

import com.jblab.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by damir on 06.07.17.
 */
@Service
public class StorageServiceImpl implements StorageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String save(MultipartFile multipartFile, String status) throws IOException {

        logger.info("StorageService: saving file started...");
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        String path = "";
        if (status.equals("upload")){
            path = "/src/main/resources/XMLs/uploaded/";
        }
        if (status.equals("delete")){
            path = "/src/main/resources/XMLs/deleted/";
        }
        File file = new File(Paths.get("").toAbsolutePath() + path +  format.format(date) + multipartFile.getOriginalFilename());
        if (!file.exists()) {
            file.createNewFile();
        }
        multipartFile.transferTo(file);
        logger.info("StorageService: saving file completed!");
        return file.getAbsolutePath();
    }


}
