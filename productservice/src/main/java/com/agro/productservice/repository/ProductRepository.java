package com.agro.productservice.repository;

import com.agro.productservice.model.Product;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Search by product name
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Filter by category
    List<Product> findByCategoryIgnoreCase(String category);

    // Filter by price range
    List<Product> findByPriceBetween(Double min, Double max);

    // Sorting
    List<Product> findAll(Sort sort);

    List<Product> findByShopkeeperId(String shopkeeperId);

    
}
