package com.agro.productservice.service;


import com.agro.productservice.dto.ProductRequest;
import com.agro.productservice.dto.ProductResponse;
import com.agro.productservice.exception.ProductNotFoundException;
import com.agro.productservice.model.Product;
import com.agro.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<ProductResponse> getAllProducts()
    {
        List<Product> products = repository.findAll();

        List<ProductResponse> responseList = new ArrayList<>();

        for(Product product : products)
        {
            ProductResponse response = new ProductResponse();

            response.setId(product.getPId());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setPrice(product.getPrice());
            response.setCategory(product.getCategory());
            response.setQuantity(product.getQuantity());
            response.setImageUrl(product.getImageUrl());
            response.setShopkeeperId(product.getShopkeeperId());

            responseList.add(response);
        }

        return responseList;
    }

    public ProductResponse getProductById(Long pId)
    {
        Product product = repository.findById(pId).orElseThrow(() -> new ProductNotFoundException("Product not found..."));

        ProductResponse response = new ProductResponse();

        response.setId(product.getPId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory());
        response.setQuantity(product.getQuantity());
        response.setImageUrl(product.getImageUrl());
        response.setShopkeeperId(product.getShopkeeperId());

        return response;
    }

    public ProductResponse updateProduct(ProductRequest request, Long pId)
    {
        Product product = repository.findById(pId).orElseThrow(() -> new ProductNotFoundException("Product not fount..."));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        product.setShopkeeperId(request.getShopkeeperId());

        Product updatedProduct = repository.save(product);

        ProductResponse response = new ProductResponse();

        response.setId(updatedProduct.getPId());
        response.setName(updatedProduct.getName());
        response.setDescription(updatedProduct.getDescription());
        response.setPrice(updatedProduct.getPrice());
        response.setCategory(updatedProduct.getCategory());
        response.setQuantity(updatedProduct.getQuantity());
        response.setImageUrl(updatedProduct.getImageUrl());
        response.setShopkeeperId(updatedProduct.getShopkeeperId());

        return response;

    }

    public String deleteProduct(Long pId)
    {
        Product product = repository.findById(pId).orElseThrow(() -> new ProductNotFoundException("Product not fount..."));

        repository.delete(product);

        return "Product detele successfully...";
    }

}