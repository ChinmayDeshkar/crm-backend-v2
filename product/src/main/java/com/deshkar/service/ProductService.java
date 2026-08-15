package com.deshkar.service;

import com.deshkar.dto.ProductRequest;
import com.deshkar.dto.ProductResponse;
import com.deshkar.model.Products;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getActiveProducts();

    Products saveProduct(ProductRequest req);

    Products getById(long id);

    void addAll(List<ProductRequest> requests);

    Products updateProductById(long id, Products product);

    void deleteProductById(long id);
}
