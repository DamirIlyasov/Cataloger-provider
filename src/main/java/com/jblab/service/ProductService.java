package com.jblab.service;

import com.jblab.model.Product;

import java.util.List;

public interface ProductService {
    void saveAll(List<Product> products);

    int deleteAllByList(List<Product> products);

    List<Product> get(int count, String uid);

    void deleteAll();
}
