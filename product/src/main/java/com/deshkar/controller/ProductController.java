package com.deshkar.controller;

import com.deshkar.dto.ProductRequest;
import com.deshkar.model.Products;
import com.deshkar.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public List<Products> getAll(){
        return productService.getActiveProducts();
    }

    @PostMapping("/add")
    public Products addProduct(@RequestBody ProductRequest product){
        log.info(product.toString());
        return productService.saveProduct(product);
    }

    @PostMapping("/add-all")
    public Products addAll(@RequestBody List<Products> products){
//        log.info(product.toString());
         productService.addAll(products);
         return null;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateById(@PathVariable long id, @RequestBody Products product){
        productService.updateProductById(id, product);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Product updated"
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id){
        productService.deleteProductById(id);
        return ResponseEntity.ok(Map.of(
                "message", "Product deleted",
                "id", id
        ));
    }
}
