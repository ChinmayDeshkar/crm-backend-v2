package com.deshkar.dto;

import com.deshkar.model.Customer;
import com.deshkar.model.PurchaseItems;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class PurchaseDetailsDTO {

    private Long purchaseId;
    private Customer customer;
    private Double price;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;
    private Double advancePaid;
    private Double balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedDate;
    private String updatedBy;
    private String remarks;

    private List<PurchaseItems> items;

}
