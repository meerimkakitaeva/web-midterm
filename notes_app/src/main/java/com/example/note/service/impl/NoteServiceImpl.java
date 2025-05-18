package com.example.note.service.impl;

import com.example.note.dto.NoteModel;
import com.example.note.entity.Note;
import com.example.note.entity.User;
import com.example.note.enums.NoteStatus;
import com.example.note.exception.NotFoundException;
import com.example.note.mapper.NoteMapper;
import com.example.note.repository.NoteRepository;
import com.example.note.repository.UserRepository;
import com.example.note.service.NoteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteMapper noteMapper;

    public NoteServiceImpl(NoteRepository noteRepository, UserRepository userRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.noteMapper = noteMapper;
    }

    @Override
    public List<NoteModel> getAllNotes() {
        return noteRepository.findAll().stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NoteModel getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Note not found with id %s", id)));
        return noteMapper.toDTO(note);
    }

    @Override
    public NoteModel createNote(NoteModel noteModel) {
        Note note = noteMapper.toEntity(noteModel);
        note.setStatus(NoteStatus.PENDING); // если NoteStatus присутствует
        note.setUser(getUserById(noteModel.getUserId()));
        note = noteRepository.save(note);
        return noteMapper.toDTO(note);
    }

    @Override
    public NoteModel updateNote(Long id, NoteModel noteModel) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (optionalNote.isPresent()) {
            Note existingNote = optionalNote.get();
            existingNote.setTitle(noteModel.getTitle());
            existingNote.setStatus(noteModel.getStatus());
            existingNote.setUser(getUserById(noteModel.getUserId()));
            noteRepository.save(existingNote);
            return noteMapper.toDTO(existingNote);
        } else {
            throw new NotFoundException(String.format("Note not found with id %s", id));
        }
    }

    @Override
    public void deleteNote(Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
        } else {
            throw new NotFoundException(String.format("Note not found with id %s", id));
        }
    }

    @Override
    public List<NoteModel> getNotesByUserId(Long userId) {
        return noteRepository.findByUserId(userId).stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User not found with id %s", userId)));
    }
}
