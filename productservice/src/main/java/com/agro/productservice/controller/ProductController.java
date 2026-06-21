package com.agro.productservice.controller;

import com.agro.productservice.dto.ProductRequest;
import com.agro.productservice.dto.ProductResponse;
import com.agro.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {


    ProductService productService;

    @PostMapping("/addProduct")
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request){

       // return ResponseEntity.ok(productService.addProduct(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(request));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts()
    {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{pId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long pId)
    {
        return ResponseEntity.ok(productService.getProductById(pId));
    }

    @PutMapping("/{pId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long pId, @RequestBody ProductRequest request)
    {
        return ResponseEntity.ok(productService.updateProduct( request ,pId));
    }

    @DeleteMapping("/{pId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long pId)
    {
        return ResponseEntity.ok(productService.deleteProduct(pId));
    }


}
