package com.example.note.mapper;

import com.example.note.dto.NoteModel;
import com.example.note.entity.Note;
import com.example.note.entity.User;
import com.example.note.enums.NoteStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NoteMapperTest {

    private NoteMapper noteMapper;
    private User user;
    private Note note;
    private NoteModel noteModel;

    @BeforeEach
    public void setUp() {
        noteMapper = new NoteMapper();

        user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);  // Return a mock user ID

        note = new Note(1L, "Test Note", NoteStatus.PENDING, user);

        noteModel = new NoteModel();
        noteModel.setId(1L);
        noteModel.setTitle("Test Note");
        noteModel.setStatus(NoteStatus.PENDING);
    }

    @Test
    void toDTOTest() {
        NoteModel noteModelConverted = noteMapper.toDTO(note);

        assertNotNull(noteModelConverted, "NoteModel should not be null");
        assertEquals(note.getId(), noteModelConverted.getId(), "Note ID should match");
        assertEquals(note.getTitle(), noteModelConverted.getTitle(), "Note title should match");
        assertEquals(note.getStatus(), noteModelConverted.getStatus(), "Note status should match");
        assertEquals(note.getUser().getId(), noteModelConverted.getUserId(), "Note user ID should match");
    }

    @Test
    void toEntityTest() {
        Note noteConverted = noteMapper.toEntity(noteModel);

        assertEquals(noteModel.getId(), noteConverted.getId(), "Note ID should match");
        assertEquals(noteModel.getTitle(), noteConverted.getTitle(), "Note title should match");
        assertEquals(noteModel.getStatus(), noteConverted.getStatus(), "Note status should match");
    }
}
