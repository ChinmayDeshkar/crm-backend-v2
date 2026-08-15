package com.deshkar.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tb_notes")
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_notes")
    private Long id;

    @Column(name = "num_purchase")
    private Long purchaseId;
    @Column(name = "cde_updated_by")
    private String updatedBy;
    @Column(name = "txt_note")
    private String note;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "dte_updated")
    private LocalDateTime dte_updated = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
}
