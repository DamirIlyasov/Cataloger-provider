package com.jblab.service.Impl;

import com.jblab.model.Journal;
import com.jblab.model.Product;
import com.jblab.repository.ProductRepository;
import com.jblab.service.JournalService;
import com.jblab.service.ProductService;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by damir on 07.07.17.
 */
@Service
@Transactional
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
        logger.info("Saving products started...");
        productRepository.save(products);
        logger.info("Products saved!");
    }

    @Override
    public int deleteAllByList(List<Product> products) {
        logger.info("-------------------------------------");
        logger.info("Deleting products started...");
        int deletedRows = 0;
        int deletedRowsTotal = 0;
        for (Product product : products) {
            String category = product.getCategory();
            String currency = product.getCurrency();
            String description = product.getDescription();
            String name = product.getName();
            String price = product.getPrice();
            String url = product.getUrl();
            deletedRows = productRepository.deleteByCategoryAndCurrencyAndDescriptionAndNameAndPriceAndUrl(category,
                    currency, description, name, price, url);
            deletedRowsTotal += deletedRows;
        }
        logger.info(deletedRowsTotal + " products deleted!");
        return deletedRowsTotal;
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
            List<Product> list = productRepository.findAllOrderedByViews(new PageRequest(0, count));
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
