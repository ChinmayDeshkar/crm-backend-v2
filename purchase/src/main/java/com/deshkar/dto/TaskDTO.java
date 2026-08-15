package com.deshkar.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDTO {
    private Long purchaseId;
    private String customerName;
    private String phoneNumber;
    private Double price;
    private Double balance;
    private String paymentStatus;
    private String orderStatus;
    private String remark;

    private LocalDateTime dte_created;
    private LocalDateTime dte_updated;

}
