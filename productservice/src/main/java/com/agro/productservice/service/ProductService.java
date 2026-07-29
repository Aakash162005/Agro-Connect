package com.agro.productservice.service;


import com.agro.productservice.dto.ProductRequest;
import com.agro.productservice.dto.ProductResponse;
import com.agro.productservice.dto.UserResponse;
import com.agro.productservice.exception.OutOfStockException;
import com.agro.productservice.exception.ProductNotFoundException;
import com.agro.productservice.exception.UserNotFoundException;
import com.agro.productservice.model.Product;
import com.agro.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ProductService {

    private ProductRepository repository;

    private final WebClient.Builder webClientBuilder;

    public ProductService(ProductRepository repository,
                          WebClient.Builder webClientBuilder) {

        this.repository = repository;
        this.webClientBuilder = webClientBuilder;
    }

    public ProductResponse addProduct(ProductRequest request, String email, String role) {

        System.out.println("========== PRODUCT SERVICE ==========");
        System.out.println("Email : " + email);
        System.out.println("Role  : " + role);
        System.out.println("=====================================");

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        UserResponse user = webClientBuilder.build()
                .get()
                .uri("lb://USER-SERVICE/api/users/email/{email}", email)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();

        product.setShopkeeperId(user.getId());

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

    public ProductResponse updateProduct(ProductRequest request, Long pId, String email, String role)
    {
        Product product = repository.findById(pId)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found"));

        UserResponse user;

        try {
            user = webClientBuilder
                    .build()
                    .get()
                    .uri("http://USER-SERVICE/api/users/email/" + email)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();

        } catch (WebClientResponseException.NotFound ex) {
            throw new UserNotFoundException("User not found");
        }

        if (!product.getShopkeeperId().equals(user.getId())
                && !role.equals("ADMIN")) {

            throw new RuntimeException(
                    "You are not allowed to update this product");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

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


    public String deleteProduct(Long pId,
                                String email,
                                String role) {

        Product product = repository.findById(pId)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found"));

        UserResponse user;

        try {

            user = webClientBuilder
                    .build()
                    .get()
                    .uri("http://USER-SERVICE/api/users/email/" + email)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();

        } catch (WebClientResponseException.NotFound ex) {

            throw new UserNotFoundException("User not found");
        }

        if (!product.getShopkeeperId().equals(user.getId())
                && !role.equals("ADMIN")) {

            throw new RuntimeException(
                    "You are not allowed to delete this product");
        }

        repository.delete(product);

        return "Product deleted successfully";
    }



    // Search
    public List<ProductResponse> searchProducts(String keyword) {

        List<Product> products =
                repository.findByNameContainingIgnoreCase(keyword);

        return convertToResponse(products);
    }

    // Category Filter
    public List<ProductResponse> getProductsByCategory(String category) {

        List<Product> products =
                repository.findByCategoryIgnoreCase(category);

        return convertToResponse(products);
    }

    // Price Filter
    public List<ProductResponse> getProductsByPrice(Double min, Double max) {

        List<Product> products =
                repository.findByPriceBetween(min, max);

        return convertToResponse(products);
    }

    // Sorting
    public List<ProductResponse> sortProducts(String field, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();

        List<Product> products = repository.findAll(sort);

        return convertToResponse(products);
    }

    private List<ProductResponse> convertToResponse(List<Product> products) {

        List<ProductResponse> responseList = new ArrayList<>();

        for (Product product : products) {

            ProductResponse response = new ProductResponse();

            response.setId(product.getPId());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setPrice(product.getPrice());
            response.setQuantity(product.getQuantity());
            response.setCategory(product.getCategory());
            response.setImageUrl(product.getImageUrl());
            response.setShopkeeperId(product.getShopkeeperId());

            responseList.add(response);
        }

        return responseList;
    }

    public List<ProductResponse> getMyProducts(String email, String role) {

        System.out.println("========== MY PRODUCTS ==========");
        System.out.println("Email : " + email);
        System.out.println("Role  : " + role);
        System.out.println("=================================");

        UserResponse user = webClientBuilder
                .build()
                .get()
                .uri("http://USER-SERVICE/api/users/email/" + email)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();

        List<Product> products =
                repository.findByShopkeeperId(user.getId());

        return convertToResponse(products);
    }

    public List<ProductResponse> getProductsByShopkeeper(String shopkeeperId) {

        List<Product> products =
                repository.findByShopkeeperId(shopkeeperId);

        return convertToResponse(products);
    }


    public ProductResponse decreaseStock(Long id, Integer quantity) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found"));

        if(product.getQuantity() < quantity){
            throw new OutOfStockException("Insufficient stock");
        }

        product.setQuantity(product.getQuantity() - quantity);

        Product updated = repository.save(product);

        ProductResponse response = new ProductResponse();

        response.setId(updated.getPId());
        response.setName(updated.getName());
        response.setDescription(updated.getDescription());
        response.setPrice(updated.getPrice());
        response.setQuantity(updated.getQuantity());
        response.setCategory(updated.getCategory());
        response.setImageUrl(updated.getImageUrl());
        response.setShopkeeperId(updated.getShopkeeperId());

        return response;
    }

    public long getProductCount() {

        return repository.count();

    }

}