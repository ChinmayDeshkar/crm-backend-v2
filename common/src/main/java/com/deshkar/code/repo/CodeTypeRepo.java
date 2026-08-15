package com.deshkar.code.repo;

import com.deshkar.code.entity.CodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeTypeRepo extends JpaRepository<CodeType, Long> {

    CodeType findByCodeType(String codeType);
}
