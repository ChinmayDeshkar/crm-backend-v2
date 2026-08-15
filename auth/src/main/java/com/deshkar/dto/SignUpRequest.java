package com.deshkar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private String salary;

}
