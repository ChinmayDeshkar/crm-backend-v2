package com.deshkar.service.impl;

import com.deshkar.dto.ProductRequest;
import com.deshkar.dto.ProductResponse;
import com.deshkar.model.Products;
import com.deshkar.repo.ProductRepo;
import com.deshkar.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    @Override
    public List<ProductResponse> getActiveProducts() {
        return createProductResponse(productRepo.findByIsActive(true));
    }

    @Override
    public Products saveProduct(ProductRequest req) {
        Products product = createProductModel(req);
        return productRepo.save(product);
    }

    @Override
    public Products getById(long id) {
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("No Product found!!"));
    }

    @Override
    public void addAll(List<ProductRequest> requests) {
        List<Products> products = createProductModelList(requests);
        productRepo.saveAll(products);
    }

    @Override
    public Products updateProductById(long id, Products product) {
        Products existing = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Error finding Product by id: " + id));
        log.debug("Found existing product: " + existing);
        product.setDte_updated(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
        log.debug(product.toString());
        return productRepo.save(product);
    }

    @Override
    public void deleteProductById(long id) {
        log.debug("Deactivating product with id: " + id);
        Products p = productRepo.findById(id).orElseThrow();
        p.setIsActive(false);   // Softly Delete
        productRepo.save(p);
    }

    // --------- Private Helper Methods ----------- //
    Products createProductModel(ProductRequest request){
        Products product = new Products();
        product.setProductName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setIsActive(true);
        product.setDte_created(LocalDateTime.now());
        product.setDte_updated(LocalDateTime.now());
        product.setUpdatedBy(getCurrentUser());
        return product;
    }

    private List<Products> createProductModelList(List<ProductRequest> requests){
        List<Products> products = new ArrayList<>();
        for(ProductRequest request: requests){
            Products product = createProductModel(request);
            products.add(product);
        }
        return products;
    }

    private String getCurrentUser(){
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private List<ProductResponse> createProductResponse(List<Products> products){
        List<ProductResponse> response = new ArrayList<>();
        for(Products product: products){
            ProductResponse res = new ProductResponse();
            res.setProductId(product.getProductId());
            res.setProductName(product.getProductName());
            res.setPrice(product.getPrice());
            res.setIsActive(product.getIsActive());
            res.setUpdatedBy(product.getUpdatedBy());
            response.add(res);
        }
        return response;
    }
}
