package com.deshkar.dto;

import com.deshkar.model.Sales;
import com.deshkar.model.Products;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseItemDto {

    private Long itemId;
    private Sales purchase;
    private Products product;
    private int quantity;
    private Double itemPrice;
}
