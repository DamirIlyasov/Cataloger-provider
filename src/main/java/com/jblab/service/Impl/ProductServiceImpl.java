package com.jblab.service.Impl;

import com.jblab.model.Journal;
import com.jblab.model.Product;
import com.jblab.repository.ProductRepository;
import com.jblab.service.JournalService;
import com.jblab.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by damir on 07.07.17.
 */
@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private JournalService journalService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, JournalService journalService) {
        this.productRepository = productRepository;
        this.journalService = journalService;
    }


    @Override
    public void saveAll(List<Product> products) {
        logger.info("-------------------------------------");
        logger.info("ProductService: saving started...");
        productRepository.save(products);
        logger.info("ProductService: saved!");
    }

    @Override
    public List<Product> get(int count, String uid) {
        logger.info("-------------------------------------");
        logger.info("ProductService: checking UID...");
        Journal journal = journalService.find(uid);
        if (journal == null) {
            logger.info("ProductService: " + "this UID is never used before! Creating new journal...");
            Journal newJournal = new Journal();
            newJournal.setUid(uid);
            logger.info("ProductService: " + "searching for " + count + " products...");
            List<Product> list = productRepository.findAllOrderedByViews(new PageRequest(0,count));
            logger.info("ProductService: " + list.size() + " products found!");
            newJournal.setProducts(list);
            journalService.save(newJournal);
            logger.info("ProductService: new journal created and saved successfully!");
            return list;
        } else {
            logger.info("ProductService: UID found!");
            logger.info("ProductService: " + "searching for " + count + " products...");
            List<Product> list = productRepository.get(uid, new PageRequest(0, count));
            logger.info("ProductService: " + list.size() + " products found!");
            if (list.size() != 0) {
                logger.info("ProductService: updating journal...");
                journal.getProducts().addAll(list);
                journalService.save(journal);
                logger.info("ProductService: Journal updated successfully!");
            }
            return list;
        }

    }

    @Override
    public void deleteAll() {
        logger.info("-------------------------------------");
        logger.info("ProductService: deleting all products...");
        productRepository.deleteAll();
        logger.info("ProductService: all products deleted successfully!");
    }
}
