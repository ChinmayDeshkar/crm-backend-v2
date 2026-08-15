package com.deshkar.service;

import com.deshkar.model.Sales;
import com.deshkar.model.Notes;

import java.util.List;

public interface NoteService {

    Notes addUpdateNote(Sales purchases, String note);

    List<Notes> getNotes(long id);
}
