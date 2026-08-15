package com.deshkar.service;

import com.deshkar.dto.PurchaseDetailsDTO;
import com.deshkar.dto.PurchaseUpdateRequest;
import com.deshkar.dto.TaskDTO;
import com.deshkar.model.Sales;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseService {

    ResponseEntity<?> addPurchase(Sales purchase);

    PurchaseDetailsDTO getPurchaseById(long id);

    List<TaskDTO> getPurchaseByCustId(long id);

    List<TaskDTO> getPurchaseByCustName(String name);

    List<TaskDTO> getPurchaseByPhoneNumber(String phoneNumber);

    List<TaskDTO> getTodayPurchases() ;

    List<TaskDTO> getPurchasesThisMonth() ;

    List<TaskDTO> getPurchasesByRange(LocalDateTime start, LocalDateTime end) ;

    List<TaskDTO> getPendingTasks();

    List<TaskDTO> getRecentTasks();

    Sales updatePurchase(long purchaseId, PurchaseUpdateRequest purchase);
}
