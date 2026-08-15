package com.deshkar.service.impl;

import com.deshkar.dto.PurchaseDetailsDTO;
import com.deshkar.dto.PurchaseUpdateRequest;
import com.deshkar.dto.TaskDTO;
import com.deshkar.exceptions.ResourceNotFoundException;
import com.deshkar.model.*;
import com.deshkar.repo.SalesRepo;
import com.deshkar.repo.CustomerRepo;
import com.deshkar.service.InvoiceService;
import com.deshkar.service.NoteService;
import com.deshkar.service.PaymentService;
import com.deshkar.service.PurchaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final CustomerRepo customerRepo;
    private final SalesRepo purchaseRepo;
    private final NoteService noteService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;


    @Override
    public ResponseEntity<?> addPurchase(Sales purchase) {
//        CustomerPurchases purchase = createPurchaseModel(dto);
        Customer payloadCustomer = purchase.getCustomer();

        log.info("Adding new Purchase : " + purchase);

        // 1. Check if customer exists
        Customer customer = customerRepo.findByPhoneNumber(payloadCustomer.getPhoneNumber())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setCustomerName(payloadCustomer.getCustomerName());
                    newCustomer.setEmail(payloadCustomer.getEmail());
                    newCustomer.setPhoneNumber(payloadCustomer.getPhoneNumber());
                    newCustomer.setAddress(payloadCustomer.getAddress());
                    newCustomer.setCreatedDate(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
                    return customerRepo.save(newCustomer);
                });

        log.info("Customer: " + customer);
        purchase.setCustomer(customer);

        // 2. Calculate balance & payment status
        if (purchase.getAdvancePaid() < purchase.getPrice()) {
            double balance = purchase.getPrice() - purchase.getAdvancePaid();
            purchase.setBalance(balance);

            if (balance > 0)
                purchase.setPaymentStatus("PENDING");
        }

        purchase.setOrderStatus("CREATED");
        log.info("Purchase received: " + purchase);
        log.info(("Items: " + purchase.getItems()));

        // 3. Process items (IMPORTANT)
        if (purchase.getItems() != null) {

            double total = 0;

            for (PurchaseItems item : purchase.getItems()) {
                // Set purchase reference
                item.setPurchase(purchase);

                // calculate total for this item
                item.setTotal(item.getQuantity() * item.getItemPrice());

                total += item.getTotal();
            }

            // override purchase price (optional)
            purchase.setPrice(total);
        }

        // 4. Save purchase along with items (cascade=ALL does the magic)
        Sales savedPurchase = purchaseRepo.save(purchase);
        customer.setPurchaseCount(customer.getPurchaseCount() + 1);
        purchase.setCreatedDate(LocalDateTime.now());
        customerRepo.save(customer);
        log.info("Purchased added. Now adding in history");

        // 5. Send to history
        noteService.addUpdateNote(savedPurchase, "Job created");

        paymentService.addPayment(purchase.getId(), purchase.getAdvancePaid(), purchase.getPaymentMethod());

        return ResponseEntity.ok(
                Map.of("message", "Purchase added successfully",
                        "customerId", customer.getId(),
                        "purchaseId", savedPurchase.getId())
        );
    }

    @Override
    public PurchaseDetailsDTO getPurchaseById(long id) {

        log.debug("Getting Purchase details for id = " + id);
        Sales purchase = purchaseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error while getting purchase with id, " + id));

        PurchaseDetailsDTO purchaseDetailsDTO = new PurchaseDetailsDTO(
                purchase.getId(),
                purchase.getCustomer(),
                purchase.getPrice(),
                purchase.getPaymentMethod(),
                purchase.getPaymentStatus(),
                purchase.getOrderStatus(),
                purchase.getAdvancePaid(),
                purchase.getBalance(),
                purchase.getCreatedDate(),
                purchase.getUpdatedDate(),
                purchase.getUpdatedBy(),
                purchase.getRemarks(),
                purchase.getItems()
        );

        return purchaseDetailsDTO;
    }

    @Override
    public List<TaskDTO> getPurchaseByCustId(long id) {

        List<Sales> purchases = purchaseRepo.findByCustomerId(id);
        if(purchases.isEmpty())
            throw new ResourceNotFoundException("No purchase with customer id: "+ id);

        List<TaskDTO> tasks = new ArrayList<>(List.of());
        for(Sales purchase: purchases){
            TaskDTO task = new TaskDTO();
            task.setPurchaseId(purchase.getId());
            task.setCustomerName(purchase.getCustomer().getCustomerName());
            task.setPhoneNumber(purchase.getCustomer().getPhoneNumber());
            task.setPrice(purchase.getPrice());
            task.setBalance(purchase.getBalance());
            task.setPaymentStatus(purchase.getPaymentStatus());
            task.setOrderStatus(purchase.getOrderStatus());
            task.setRemark(purchase.getRemarks());
            task.setDte_created(purchase.getCreatedDate());
            tasks.add(task);
        }

        return tasks;
    }

    @Override
    public List<TaskDTO> getPurchaseByCustName(String name) {
        log.info("Customer name: " + name);
        List<Sales> purchases = purchaseRepo.findByCustomerName(name);
        if(purchases.isEmpty())
            throw new ResourceNotFoundException("No purchase with customer name: "+ name);

        List<TaskDTO> tasks = new ArrayList<>(List.of());
        for(Sales purchase: purchases){
            TaskDTO task = new TaskDTO();
            task.setPurchaseId(purchase.getId());
            task.setCustomerName(purchase.getCustomer().getCustomerName());
            task.setPhoneNumber(purchase.getCustomer().getPhoneNumber());
            task.setPrice(purchase.getPrice());
            task.setBalance(purchase.getBalance());
            task.setPaymentStatus(purchase.getPaymentStatus());
            task.setOrderStatus(purchase.getOrderStatus());
            task.setRemark(purchase.getRemarks());
            task.setDte_created(purchase.getCreatedDate());
            tasks.add(task);
        }

        return tasks;
    }

    @Override
    public List<TaskDTO> getPurchaseByPhoneNumber(String phoneNumber) {
        Customer customer = customerRepo.findByPhoneNumber(phoneNumber).orElseThrow(() -> new ResourceNotFoundException("Purchase not found with phone number " + phoneNumber));
        return getPurchaseByCustId(customer.getId());
    }

    private List<TaskDTO> mapToDTO(List<Sales> purchases) {
        List<TaskDTO> tasks = new ArrayList<>(List.of());
        for(Sales purchase: purchases){
            TaskDTO task = new TaskDTO();
            task.setPurchaseId(purchase.getId());
            task.setCustomerName(purchase.getCustomer().getCustomerName());
            task.setPhoneNumber(purchase.getCustomer().getPhoneNumber());
            task.setPrice(purchase.getPrice());
            task.setBalance(purchase.getBalance());
            task.setPaymentStatus(purchase.getPaymentStatus());
            task.setOrderStatus(purchase.getOrderStatus());
            task.setRemark(purchase.getRemarks());
            task.setDte_created(purchase.getCreatedDate());
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    public List<TaskDTO> getTodayPurchases() {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        log.info("Fetching purchases between {} and {}", startOfDay, endOfDay);
        List<Sales> purchases = purchaseRepo.findByCreatedDateBetween(startOfDay, endOfDay);

        return mapToDTO(purchases);
    }

    @Override
    public List<TaskDTO> getPurchasesThisMonth() {
        List<Sales> purchases = purchaseRepo.findPurchasesThisMonth();
        return mapToDTO(purchases);
    }

    @Override
    public List<TaskDTO> getPurchasesByRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching purchases from {} to {}", startDate, endDate);
        List<Sales> purchases = purchaseRepo.findByCreatedDateBetween(startDate, endDate);
        return mapToDTO(purchases);
    }


    @Override
    public List<TaskDTO> getPendingTasks() {
        List<Sales> purchases = purchaseRepo.findPendingTasks();
        List<TaskDTO> tasks = new ArrayList<>(List.of());
        for(Sales purchase: purchases){
            TaskDTO task = new TaskDTO();
            task.setPurchaseId(purchase.getId());
            task.setCustomerName(purchase.getCustomer().getCustomerName());
            task.setPhoneNumber(purchase.getCustomer().getPhoneNumber());
            task.setPrice(purchase.getPrice());
            task.setBalance(purchase.getBalance());
            task.setPaymentStatus(purchase.getPaymentStatus());
            task.setOrderStatus(purchase.getOrderStatus());
            task.setRemark(purchase.getRemarks());
            task.setDte_created(purchase.getCreatedDate());
            task.setDte_updated(purchase.getUpdatedDate());
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    public List<TaskDTO> getRecentTasks() {
            List<String> statuses = List.of("COMPLETED", "DELIVERED", "CANCELLED");
        LocalDateTime fromDate = LocalDateTime.now(ZoneId.of("Asia/Kolkata")).minusDays(2);

        List<Sales> purchases = purchaseRepo.findRecentCompletedOrders(statuses, fromDate);
        List<TaskDTO> tasks = new ArrayList<>(List.of());
        for(Sales purchase: purchases){
            TaskDTO task = new TaskDTO();
            task.setPurchaseId(purchase.getId());
            task.setCustomerName(purchase.getCustomer().getCustomerName());
            task.setPhoneNumber(purchase.getCustomer().getPhoneNumber());
            task.setPrice(purchase.getPrice());
            task.setBalance(purchase.getBalance());
            task.setPaymentStatus(purchase.getPaymentStatus());
            task.setOrderStatus(purchase.getOrderStatus());
            task.setRemark(purchase.getRemarks());
            task.setDte_created(purchase.getCreatedDate());
            task.setDte_updated(purchase.getUpdatedDate());
            tasks.add(task);
        }
        return tasks;
//        return purchaseRepo.findRecentCompletedOrders(statuses, fromDate);
    }

    @Override
    public Sales updatePurchase(long purchaseId, PurchaseUpdateRequest newPurchase) {

        // Load existing purchase (managed entity)
        Sales oldPurchase = purchaseRepo.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("PurchaseId not found: " + purchaseId));

        List<String> notes = addNotes(newPurchase, oldPurchase);

        if (newPurchase.getCustomerUpdated()) {
            Customer existingCustomer = oldPurchase.getCustomer();  // managed entity

            Customer updatedData = newPurchase.getCustomer();

            existingCustomer.setCustomerName(updatedData.getCustomerName());
            existingCustomer.setPhoneNumber(updatedData.getPhoneNumber());
            existingCustomer.setAddress(updatedData.getAddress());
            existingCustomer.setEmail(updatedData.getEmail());
        }

        double amount = newPurchase.getAdvancePaid() - oldPurchase.getAdvancePaid();
        // Update simple fields
        oldPurchase.setPrice(newPurchase.getPrice());
        oldPurchase.setPaymentMethod(newPurchase.getPaymentMethod());
        oldPurchase.setPaymentStatus(newPurchase.getPaymentStatus());
        oldPurchase.setOrderStatus(newPurchase.getOrderStatus());
        oldPurchase.setAdvancePaid(newPurchase.getAdvancePaid());
        oldPurchase.setBalance(newPurchase.getBalance());
        oldPurchase.setRemarks(newPurchase.getRemarks());
        oldPurchase.setUpdatedBy(newPurchase.getUpdatedBy());
        oldPurchase.setUpdatedDate(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));

        oldPurchase.getItems().clear();

        for (PurchaseItems item : newPurchase.getItems()) {
            item.setItemId(null);
            item.setPurchase(oldPurchase);
            oldPurchase.getItems().add(item);
        }

        Sales saved = purchaseRepo.save(oldPurchase);
        if(amount > 0)
            paymentService.addPayment(saved.getId(), amount, saved.getPaymentMethod());


        // Save notes
        for (String n : notes) {
            noteService.addUpdateNote(saved, n);
        }

        // If OrderStatus == READY -> generate invoice
        if (newPurchase.getOrderStatus().equals("READY")) {
            try {
                invoiceService.generateInvoice(newPurchase.getPurchaseId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return saved;
    }

    List<String> addNotes(PurchaseUpdateRequest newPurchase, Sales oldPurchase){
        List<String> notes = new ArrayList<>();

        // --- CHECK CHANGES ---
        if (!Objects.equals(oldPurchase.getOrderStatus(), newPurchase.getOrderStatus())) {

            notes.add("Order status updated | "
                    + oldPurchase.getOrderStatus() + " → " + newPurchase.getOrderStatus());
        }

        if (!Objects.equals(oldPurchase.getPaymentStatus(), newPurchase.getPaymentStatus())) {
            notes.add("Payment status updated | "
                    + oldPurchase.getPaymentStatus() + " → " + newPurchase.getPaymentStatus());
        }

        if (!Objects.equals(oldPurchase.getAdvancePaid(), newPurchase.getAdvancePaid())) {
            notes.add("Advance updated | "
                    + oldPurchase.getAdvancePaid() + " → " + newPurchase.getAdvancePaid());
        }

        if (!Objects.equals(oldPurchase.getRemarks(), newPurchase.getRemarks())) {
            notes.add("Remarks updated");
        }

        return notes;
    }

    Payment addPayment(long purchaseId, double amount, String paymentType){
        return null;
    }


    // ---------- Private Helper Methods ------------ //
    Sales createPurchaseModel(PurchaseDetailsDTO dto){
        Sales purchase = new Sales();
        purchase.setId(dto.getPurchaseId());
        purchase.setCustomer(dto.getCustomer());
        purchase.setPrice(dto.getPrice());
        purchase.setPaymentMethod(dto.getPaymentMethod());
        purchase.setPaymentStatus(dto.getPaymentStatus());
        purchase.setOrderStatus(dto.getOrderStatus());
        purchase.setAdvancePaid(dto.getAdvancePaid());
        purchase.setBalance(dto.getBalance());
        purchase.setCreatedDate(dto.getCreatedDate());
        purchase.setUpdatedDate(dto.getUpdatedDate());
        purchase.setUpdatedBy(dto.getUpdatedBy());
        purchase.setRemarks(dto.getRemarks());
        purchase.setItems(dto.getItems());

        return purchase;
    }
}
