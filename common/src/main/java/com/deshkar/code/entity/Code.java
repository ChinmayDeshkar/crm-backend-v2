package com.deshkar.code.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "tb_code")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_code")
    private Long id;

    @Column(name = "cde_code")
    private String code;

    @Column(name = "num_codetype")
    private Long codeTypeId;

    @Column(name = "txt_shortdesc")
    private String shortDesc;

    @Column(name = "txt_desc")
    private String desc;

    @Column(name = "dte_created")
    private LocalDateTime createdDate = LocalDateTime.now();
}
