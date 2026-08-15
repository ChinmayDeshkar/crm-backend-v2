package com.deshkar.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Code {

    private Long id;
    private String code;
    private String codeType;
}
