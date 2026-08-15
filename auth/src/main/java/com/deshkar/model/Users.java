package com.deshkar.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@Table(name = "tb_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "cde_username"),
        @UniqueConstraint(columnNames = "txt_email"),
        @UniqueConstraint(columnNames = "txt_phone")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_user")
    private Long id;

    @Column(nullable = false, length = 50, name = "cde_username")
    private String username;

    @Column(nullable = false, name = "txt_firstname")
    private String firstName;

    @Column(nullable = false, name = "txt_lastname")
    private String lastName;

    @Column(nullable = false, name = "txt_password")
    private String password; // stored hashed

    @Column(nullable = false, length = 100, name = "txt_email")
    private String email;

    @Column(nullable = false, length = 15, name = "txt_phone")
    private String phone;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, name = "cde_role")
//    private Role role;
    @Column(name = "num_role")
    private Long role;

    @NotNull
    @Column(name = "txt_salary")
    private String salary;
    @Column(name = "flg_active")
    private Boolean isActive = true;
    @Column(name = "dte_created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dte_created = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

    @Column(name = "flg_firstlogin")
    private Boolean firstLogin = true;
}
