package com.deshkar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tb_customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_customer")
    private Long id;

    @Column(name = "txt_customer_name")
    private String customerName;
    @Column(name = "txt_email")
    private String email;

    @Column(unique = true, nullable = false, name = "txt_phone")
    private String phoneNumber;

    @Column(name = "txt_address")
    private String address;
    @Column(name = "dte_created")
    private LocalDateTime createdDate = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    @JsonIgnore
    private List<Sales> purchases;

    @Nullable
    @Column(name = "purchase_count")
    private Long purchaseCount = 0L;
}