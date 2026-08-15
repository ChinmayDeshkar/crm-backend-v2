package com.deshkar.dto;

import com.deshkar.model.Sales;
import com.deshkar.model.Products;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseItemDto {

    private Products product;
    private int quantity;
    private Double itemPrice;
}
