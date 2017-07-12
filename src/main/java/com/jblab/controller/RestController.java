package com.jblab.controller;

import com.jblab.model.Product;
import com.jblab.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by damir on 07.07.17.
 */
@Controller
public class RestController {

    private ProductService productService;

    @Autowired
    public RestController(ProductService productService) {
        this.productService = productService;
    }

    @ResponseBody
    @RequestMapping(value = "/get")
    public List<Product> getProducts(@RequestParam(value = "count") int count,
                                     @RequestParam(value = "uid") String uid) {
       return productService.get(count,uid);
    }
}
