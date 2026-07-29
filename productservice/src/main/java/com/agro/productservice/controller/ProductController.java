package com.agro.productservice.controller;

import com.agro.productservice.dto.DashboardProductResponse;
import com.agro.productservice.dto.ProductRequest;
import com.agro.productservice.dto.ProductResponse;
import com.agro.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {


    ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(
            @Valid @RequestBody ProductRequest request,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(request, email, role));
    }

    @GetMapping()
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
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long pId,
            @RequestBody ProductRequest request,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role) {

        return ResponseEntity.ok(
                productService.updateProduct(request, pId, email, role));
    }

    @DeleteMapping("/{pId}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long pId,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role) {

        return ResponseEntity.ok(
                productService.deleteProduct(pId, email, role));
    }

    // Search Product
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String keyword) {

        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    // Filter by Category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    // Filter by Price
    @GetMapping("/price")
    public ResponseEntity<List<ProductResponse>> getProductsByPrice(
            @RequestParam Double min,
            @RequestParam Double max) {

        return ResponseEntity.ok(productService.getProductsByPrice(min, max));
    }

    // Sorting
    @GetMapping("/sort")
    public ResponseEntity<List<ProductResponse>> sortProducts(
            @RequestParam String field,
            @RequestParam(defaultValue = "asc") String direction) {

        return ResponseEntity.ok(
                productService.sortProducts(field, direction)
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<ProductResponse>> getMyProducts(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role) {

        return ResponseEntity.ok(productService.getMyProducts(email, role));
    }


    @GetMapping("/shopkeeper/{shopkeeperId}")
    public ResponseEntity<List<ProductResponse>> getProductsByShopkeeper(
            @PathVariable String shopkeeperId) {

        return ResponseEntity.ok(
                productService.getProductsByShopkeeper(shopkeeperId));
    }

    @PatchMapping("/{id}/decrease-stock")
    public ResponseEntity<ProductResponse> decreaseStock(
            @PathVariable Long id,
            @RequestParam Integer quantity){

        return ResponseEntity.ok(
                productService.decreaseStock(id, quantity));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getProductCount() {

        return ResponseEntity.ok(
                productService.getProductCount());

    }

    @GetMapping("/statistics")
    public ResponseEntity<DashboardProductResponse> getStatistics() {

        return ResponseEntity.ok(
                productService.getStatistics());
    }

}
