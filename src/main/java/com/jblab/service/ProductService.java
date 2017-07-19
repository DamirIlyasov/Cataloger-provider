package com.jblab.service;

import com.jblab.model.Product;

import java.util.List;

/**
 * Created by damir on 07.07.17.
 */
public interface ProductService {
    void saveAll(List<Product> products);

    List<Product> get(int count, String uid);

    void deleteAll();
}
