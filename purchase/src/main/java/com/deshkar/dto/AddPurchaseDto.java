package com.deshkar.dto;

import com.deshkar.model.Customer;
import com.deshkar.model.PurchaseItems;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
public class AddPurchaseDto {

    private CustomerDto customer;
    private List<PurchaseItemDto> items;
    private Double price;
    private String paymentMethod;
    private String paymentStatus;
    private Double advancePaid;
    private Double balance;
    private String remarks;
}
