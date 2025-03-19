package com.example.note.service;

import com.example.note.dto.NoteModel;

import java.util.List;

public interface NoteService {

    List<NoteModel> getAllNotes();

    NoteModel getNoteById(Long id);

    NoteModel createNote(NoteModel noteModel);

    NoteModel updateNote(Long id, NoteModel noteModel);

    void deleteNote(Long id);

    List<NoteModel> getNotesByUserId(Long userId);
}
