package com.jblab.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String save(MultipartFile multipartFile, String status) throws IOException;
}
