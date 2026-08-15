package com.deshkar.code.repo;

import com.deshkar.code.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepo extends JpaRepository<Code, Long> {
    List<Code> findByCode(String code);
}
