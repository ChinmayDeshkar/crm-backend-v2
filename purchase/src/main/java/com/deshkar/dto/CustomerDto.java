package com.deshkar.dto;

import lombok.Data;

@Data
public class CustomerDto {

    private Long id;
    private String customerName;
    private String email;
    private String phoneNumber;
    private String address;
}
