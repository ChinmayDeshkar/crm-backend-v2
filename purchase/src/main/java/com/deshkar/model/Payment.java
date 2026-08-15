package com.deshkar.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tb_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_payment")
    private Long paymentId;
//    @ManyToOne
//    @JoinColumn(name = "purchase_id", nullable = false)
    @Column(name = "num_purchase_id")
    private Long purchaseId;
    @Column(name = "amt_total")
    private Double amount;
    @Column(name = "dte_payment")
    private LocalDate paymentDate;
    @Column(name = "cde_payment_type")
    private String paymentType;

}
