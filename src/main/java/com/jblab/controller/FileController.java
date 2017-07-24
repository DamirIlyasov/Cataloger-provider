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
    public String uploadFileAndSaveProductsAndFile(@RequestParam("file") MultipartFile multipartFile, Model model) {
        if (!multipartFile.isEmpty()) {
            String fileName = multipartFile.getOriginalFilename();
            String path = null;
            try {
                path = storageService.save(multipartFile, "upload");
            } catch (IOException e) {
                model.addAttribute("message", "Error: " + e.getMessage());
                e.printStackTrace();
                return "upload";
            }
            try {
                List<Product> products = parserService.parse(path, fileName);
                if (products != null) {
                    productService.saveAllAndAddIdToReadableName(products);
                } else {
                    model.addAttribute("message", "Error: ParseError!");
                }
            } catch (Exception e) {
                String message = "";
                try {
                    storageService.delete(path);
                    message = "Error while deleting file!";
                } catch (IOException e1) {
                    model.addAttribute("message", message + "Error: " + e.getMessage());
                    e.printStackTrace();
                    return "upload";
                }
            }
            model.addAttribute("message", "Successfully uploaded " + multipartFile.getOriginalFilename());
        } else {
            model.addAttribute("message", "Error: Empty file!");
        }
        return "upload";
    }

    @RequestMapping(value = "/delete")
    public String getDeletePage() {
        return "delete";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteProductsFromFileAndSaveFile(@RequestParam("file") MultipartFile multipartFile, Model model) {
        int deletedProductsCount = 0;
        String path = null;
        if (!multipartFile.isEmpty()) {
            try {
                String fileName = multipartFile.getOriginalFilename();
                path = storageService.save(multipartFile, "delete");
                List<Product> products = parserService.parse(path, fileName);
                if (products != null) {
                    deletedProductsCount = productService.deleteAllByList(products);
                } else {
                    model.addAttribute("message", "Error: ParseError!");
                }
            } catch (Exception e) {
                String message = "";
                try {
                    storageService.delete(path);
                    message = "Error while deleting file!";
                } catch (IOException e1) {
                    model.addAttribute("message", message + "Error: " + e.getMessage());
                    e.printStackTrace();
                    return "delete";
                }
            }
            model.addAttribute("message", deletedProductsCount + " products successfully deleted! " + multipartFile.getOriginalFilename());
        } else {
            model.addAttribute("message", "Error: empty file!");
        }
        return "delete";
    }

}
