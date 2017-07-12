package com.jblab.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by damir on 11.07.17.
 */
@Entity
public class Journal {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Product> products;
    private String uid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
