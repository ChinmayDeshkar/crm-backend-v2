package com.deshkar.dto;

import com.deshkar.model.Customer;
import com.deshkar.model.PurchaseItems;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class PurchaseUpdateRequest {

    private Long purchaseId;
    private Customer customer;
    private List<PurchaseItems> items;
    private Double price;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;
    private Double advancePaid;
    private Double balance;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedDate;
    private String updatedBy;
    private String remarks;
    Boolean customerUpdated;

}
