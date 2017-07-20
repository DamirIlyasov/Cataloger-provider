package com.jblab.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by damir on 07.07.17.
 */
public interface StorageService {
    String save(MultipartFile multipartFile, String status) throws IOException;
}
