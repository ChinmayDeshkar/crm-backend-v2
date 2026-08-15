package com.deshkar.code.service.impl;

import com.deshkar.code.dto.Code;
import com.deshkar.code.entity.CodeType;
import com.deshkar.code.repo.CodeRepo;
import com.deshkar.code.repo.CodeTypeRepo;
import com.deshkar.code.service.CodeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CodeServiceImpl implements CodeService {

    private final CodeRepo codeRepo;
    private final CodeTypeRepo codeTypeRepo;

    @Override
    public Code getCode(Long codeId) {
        com.deshkar.code.entity.Code code = codeRepo.findById(codeId)
                .orElseThrow(() -> new RuntimeException("Code not Found.. "));

        CodeType codeType = codeTypeRepo.findById(code.getCodeTypeId()).
        orElseThrow(() -> new RuntimeException("Code Type not found"));

        return new Code(code.getId(), code.getCode(),codeType.getCodeType());
    }

    @Override
    public Code getCode(String code, String codeType) {
        if(code == null || codeType == null){
            return null;
        }
        CodeType codeType1 = codeTypeRepo.findByCodeType(codeType);

        List<com.deshkar.code.entity.Code> codeEntities = codeRepo.findByCode(code);
        for(com.deshkar.code.entity.Code codeEntity : codeEntities){
            if (Objects.equals(codeEntity.getCodeTypeId(), codeType1.getId())){
                return new Code(codeEntity.getId(), codeEntity.getCode(), codeType1.getCodeType());
            }
        }
        return null;
    }

}
