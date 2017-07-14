package com.jblab.repository;

import com.jblab.model.Product;
import com.jblab.model.Journal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by damir on 07.07.17.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM product WHERE product.id NOT IN (SELECT products_id FROM  journal_products WHERE " +
            "(products_id = ANY (SELECT products_id FROM journal_products WHERE " +
            "journal_id = (SELECT journal.id FROM journal WHERE journal.uid = ?1))))" +
            " order BY ?#{#pageable}", nativeQuery = true)
    List<Product> get(String uid, Pageable pageable);

}
