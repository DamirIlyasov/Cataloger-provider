package com.jblab.repository;

import com.jblab.model.Product;
import com.jblab.model.Journal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM product WHERE product.id NOT IN (SELECT products_id FROM  journal_products WHERE " +
            "(products_id = ANY (SELECT products_id FROM journal_products WHERE " +
            "journal_id = (SELECT journal.id FROM journal WHERE journal.uid = ?1))))" +
            " ORDER BY product.count_views ASC , ?#{#pageable} ,random() ", nativeQuery = true)
    List<Product> get(String uid, Pageable pageable);

    @Query(value = "SELECT * FROM product ORDER BY product.count_views ASC , ?#{#pageable} ,random()", nativeQuery = true)
    List<Product> findAllOrderedByViews(Pageable pageable);

    @Modifying
    int deleteByCategoryAndCurrencyAndDescriptionAndNameAndPriceAndUrl(String category, String currency,
                                                                       String description, String name,
                                                                       String price, String url);
    Product findOneByUrl(String url);
}
