package com.agro.productservice.controller;

import com.agro.productservice.dto.ProductRequest;
import com.agro.productservice.dto.ProductResponse;
import com.agro.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {


    ProductService productService;

    @PostMapping("/addProduct")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest request){

       // return ResponseEntity.ok(productService.addProduct(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(request));
    }


}
