package com.deshkar.service;

import com.deshkar.dto.ProductRequest;
import com.deshkar.model.Products;

import java.util.List;

public interface ProductService {

    List<Products> getActiveProducts();

    Products saveProduct(ProductRequest req);

    Products getById(long id);

    void addAll(List<Products> products);

    Products updateProductById(long id, Products product);

    void deleteProductById(long id);
}
