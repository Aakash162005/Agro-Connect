package com.agro.productservice.service;


import com.agro.productservice.dto.ProductRequest;
import com.agro.productservice.dto.ProductResponse;
import com.agro.productservice.model.Product;
import com.agro.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public ProductResponse addProduct(ProductRequest request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        product.setShopkeeperId(request.getShopkeeperId());

        Product savedProduct = repository.save(product);

        ProductResponse response = new ProductResponse();

        response.setId(savedProduct.getPId());
        response.setName(savedProduct.getName());
        response.setDescription(savedProduct.getDescription());
        response.setPrice(savedProduct.getPrice());
        response.setQuantity(savedProduct.getQuantity());
        response.setCategory(savedProduct.getCategory());
        response.setImageUrl(savedProduct.getImageUrl());
        response.setShopkeeperId(savedProduct.getShopkeeperId());

        return response;
    }

}