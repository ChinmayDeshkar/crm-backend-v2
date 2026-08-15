package com.deshkar.service;

import com.deshkar.model.Sales;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CustomerService {
    boolean customerExists(String phoneNumber) ;
    Sales addPurchase(Map<String, Object> payload) ;
    ResponseEntity<?> customerDetails(String phoneNumber);

}
