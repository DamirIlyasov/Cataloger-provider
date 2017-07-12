package com.jblab.controller;

import com.jblab.model.Product;
import com.jblab.service.ParseService;
import com.jblab.service.ProductService;
import com.jblab.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by damir on 05.07.17.
 */
@Controller
public class FileController {

    private StorageService storageService;
    private ParseService parserService;
    private ProductService productService;

    @Autowired
    FileController(StorageService storageService, ParseService parserService, ProductService productService) {
        this.storageService = storageService;
        this.parserService = parserService;
        this.productService = productService;
    }

    @RequestMapping("/upload")
    public String getUploadPage() {
        return "upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile multipartFile, Model model) {
        if (!multipartFile.isEmpty()) {
            try {
                String path = storageService.save(multipartFile);
                List<Product> products = parserService.parse(path);
                productService.saveAll(products);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                model.addAttribute("message", "Error: " + e.getMessage());
                e.printStackTrace();
                return "upload";
            }
            model.addAttribute("message", "Successfully uploaded " + multipartFile.getOriginalFilename());
        }
        return "upload";
    }

}
