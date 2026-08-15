package com.deshkar.code.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tb_codetype")
public class CodeType {

    public final static String ROLE = "ROLE";
    public final static String ORDER_STATUS = "ORDER_STATUS";
    public final static String PAYMENT_STATUS = "PAYMENT_STATUS";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_codetype")
    private Long id;

    @Column(name = "cde_codetype")
    private String codeType;

    @Column(name = "txt_shortdesc")
    private String shortDesc;

    @Column(name = "txt_desc")
    private String desc;

    @Column(name = "dte_created")
    private LocalDateTime createdDate = LocalDateTime.now();
}
