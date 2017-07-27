package com.jblab.service;

import com.jblab.model.Product;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface ParseService {
    List<Product> parse(MultipartFile multipartFile, String fileName) throws IOException, SAXException, ParserConfigurationException;
}
