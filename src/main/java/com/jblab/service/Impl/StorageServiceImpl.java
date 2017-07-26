package com.jblab.service.Impl;

import com.jblab.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@PropertySource(value = "classpath:application.properties")
public class StorageServiceImpl implements StorageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Environment environment;

    @Autowired
    public StorageServiceImpl(Environment environment) {
        this.environment = environment;
    }

    public String save(MultipartFile multipartFile, String status) throws IOException {

        logger.info("StorageService: saving file started...");
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        String path = "";
        String envPath = environment.getProperty("storage.path");
        String currentPcName = System.getProperty("user.name");
        if (status.equals("upload")) {
            if (envPath == null) {
                path = "/home/" + currentPcName + "/storage/uploaded/";
            } else {
                if (envPath.equals("")) {
                    path = "/home/" + currentPcName + "/storage/uploaded/";
                } else {
                    path = envPath;
                }
            }
        }
        if (status.equals("delete")) {
            if (envPath == null) {
                path = "/home/" + currentPcName + "/storage/deleted/";
            } else {
                if (envPath.equals("")) {
                    path = "/home/" + currentPcName + "/storage/deleted/";
                } else {
                    path = envPath;
                }
            }
        }
        File dirs = new File(path);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        String pathForSaveFile = path + format.format(date) + multipartFile.getOriginalFilename();
        logger.info("Saving file in : " + pathForSaveFile);
        File file = new File(pathForSaveFile);
        if (!file.exists()) {
            file.createNewFile();
        }
        multipartFile.transferTo(file);
        logger.info("StorageService: saving file completed!");
        return file.getAbsolutePath();
    }

    @Override
    public void delete(String stringPath) throws IOException {
        Path path = Paths.get(stringPath);
        Files.delete(path);
    }


}
