package com.deshkar.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tb_product")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_product")
    private Long productId;
    @NotNull
    @Column(name = "cde_product_name")
    private String productName;
    @NotNull
    @Column(name = "amt_price")
    private Double price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dte_created = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dte_updated = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "flg_active")
    private Boolean isActive = true;

}
