package com.deshkar.service.impl;

import com.deshkar.model.Sales;
import com.deshkar.model.Notes;
import com.deshkar.repo.UpdateNotesRepo;
import com.deshkar.service.NoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final UpdateNotesRepo updateNotesRepo;

    @Override
    public Notes addUpdateNote(Sales purchases, String note) {
        Notes notes = new Notes();
        notes.setPurchaseId(purchases.getId());
        notes.setUpdatedBy(purchases.getUpdatedBy());
        notes.setNote(note);

        updateNotesRepo.save(notes);
        return null;
    }

    @Override
    public List<Notes> getNotes(long id) {

        return updateNotesRepo.findByPurchaseId(id);
    }
}
