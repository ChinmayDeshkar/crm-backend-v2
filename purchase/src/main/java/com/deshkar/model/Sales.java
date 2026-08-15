package com.deshkar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tb_sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_sales")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "num_customer", nullable = false)
    @JsonBackReference
    private Customer customer;
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItems> items;
    private Double price;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;
    private Double advancePaid;
    private Double balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdDate = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedDate;
    private String updatedBy;
    private String remarks;

    @PostPersist
    protected void onUpdated(){ updatedDate = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));}
}