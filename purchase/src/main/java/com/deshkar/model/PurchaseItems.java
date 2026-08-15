package com.deshkar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tb_purchaseItems")
public class PurchaseItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_purchase_item")
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "num_purchase")
    @JsonBackReference
    private Sales purchase;

    @ManyToOne
    @JoinColumn(name = "num_product")
    private Products product;

    @Column(name = "num_qty")
    private Integer quantity;

    @Column(name = "amt_price")
    private Double itemPrice; // allow override if needed (e.g. discount)

    @Column(name = "amt_total")
    private Double total; // quantity * itemPrice
}
