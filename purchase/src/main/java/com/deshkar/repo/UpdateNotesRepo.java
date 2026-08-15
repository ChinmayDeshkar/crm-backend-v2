package com.deshkar.repo;

import com.deshkar.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpdateNotesRepo extends JpaRepository<Notes, Long> {

    List<Notes> findByPurchaseId(long id);

}
