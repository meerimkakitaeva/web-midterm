package com.example.note.controller;

import com.example.note.dto.NoteModel;
import com.example.note.response.ResponseApi;
import com.example.note.response.ResponseCode;
import com.example.note.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteControllerTest {

    private final NoteService noteService;

    public NoteControllerTest(NoteService noteService) {
        this.noteService = noteService;
    }

    @Operation(summary = "Get all notes", description = "Retrieve a list of all notes")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all notes", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NoteModel.class)))
    @GetMapping
    public ResponseApi<List<NoteModel>> getAllNotes() {
        List<NoteModel> notes = noteService.getAllNotes();
        return new ResponseApi<>(notes, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get notes by user ID", description = "Retrieve notes assigned to a specific user by their user ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved notes by user ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NoteModel.class)))
    @GetMapping("/byUser")
    public ResponseApi<List<NoteModel>> getNotesByUserId(@Parameter(description = "The ID of the user to filter notes by") @RequestParam Long userId) {
        List<NoteModel> notes = noteService.getNotesByUserId(userId);
        return new ResponseApi<>(notes, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Get note by ID", description = "Retrieve a specific note by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved note", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NoteModel.class)))
    @ApiResponse(responseCode = "404", description = "Note not found")
    @GetMapping("/{id}")
    public ResponseApi<NoteModel> getNoteById(@Parameter(description = "The ID of the note to retrieve") @PathVariable Long id) {
        NoteModel note = noteService.getNoteById(id);
        return new ResponseApi<>(note, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Create a new note", description = "Create a new note with the provided note model")
    @ApiResponse(responseCode = "201", description = "Successfully created note", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NoteModel.class)))
    @PostMapping
    public ResponseApi<NoteModel> createNote(@RequestBody @Valid NoteModel noteModel) {
        NoteModel createdNote = noteService.createNote(noteModel);
        return new ResponseApi<>(createdNote, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Update a note", description = "Update an existing note with the provided note model")
    @ApiResponse(responseCode = "200", description = "Successfully updated note", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NoteModel.class)))
    @ApiResponse(responseCode = "404", description = "Note not found")
    @PutMapping("/{id}")
    public ResponseApi<NoteModel> updateNote(@PathVariable Long id, @RequestBody @Valid NoteModel noteModel) {
        NoteModel updatedNote = noteService.updateNote(id, noteModel);
        return new ResponseApi<>(updatedNote, ResponseCode.SUCCESS);
    }

    @Operation(summary = "Delete a note", description = "Delete a note by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully deleted note", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Note not found")
    @DeleteMapping("/{id}")
    public ResponseApi<String> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return new ResponseApi<>("Successfully deleted", ResponseCode.SUCCESS);
    }
}
