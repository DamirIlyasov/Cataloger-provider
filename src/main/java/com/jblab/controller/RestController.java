package com.jblab.controller;

import com.jblab.model.Product;
import com.jblab.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RestController(ProductService productService) {
        this.productService = productService;
    }

    @ResponseBody
    @RequestMapping(value = "/get")
    public List<Product> getProducts(@RequestParam(value = "count") int count,
                                     @RequestParam(value = "uid") String uid) {
        List<Product> products = productService.get(count, uid);
        logger.info("Updating viewCounters...");
//        for (int i = 0; i < products.size(); i++) {
//            int counter = products.get(i).getCountViews();
//            logger.info("Old viewCounter: " + counter);
//            products.get(i).setCountViews(++counter);
//            logger.info("New viewCounter: " + products.get(i).getCountViews());
//        }
        for (Product product : products) {
            int countViews = product.getCountViews();
            product.setCountViews(++countViews);
        }
        productService.saveAll(products);
        logger.info("ViewCounters updated!");
        return products;
    }
}
