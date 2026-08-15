package com.deshkar.code.service;

import com.deshkar.code.dto.Code;

public interface CodeService {

    Code getCode(Long codeId);
    Code getCode(String code, String CodeType);

}
