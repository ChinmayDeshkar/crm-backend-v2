package com.deshkar.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tb_invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_invoice")
    private Long invoiceId;

    @Column(name = "cde_invoice_no")
    private String invoiceNumber;   // Auto-generated like INV-202501-0001

    @Column(name = "num_purchase")
    private Long purchaseId;

    @Column(name = "txt_customer_name")
    private String customerName;
    @Column(name = "txt_customer_phone")
    private String customerPhone;
    @Column(name = "txt_customer_email")
    private String customerEmail;

    @Column(name = "dte_invoice")
    private LocalDateTime invoiceDate;

    @Column(name = "amt_total")
    private Double totalAmount;
    @Column(name = "amt_paid")
    private Double paidAmount;
    @Column(name = "amt_balance")
    private Double balanceAmount;
    @Column(name = "cde_payment_method")
    private String paymentMethod;

    @Column(name = "status")
    private String status; // GENERATED, DELIVERED

    @Column(name = "txt_file_path")
    private String pdfFilePath; // Saved file path
}
