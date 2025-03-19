package com.example.note.mapper;

import com.example.note.dto.NoteModel;
import com.example.note.dto.UserModel;
import com.example.note.entity.Note;
import com.example.note.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    @Mock
    private NoteMapper noteMapper;

    @Mock
    private Note note1;

    @Mock
    private Note note2;

    @Mock
    private NoteModel noteModel1;

    @Mock
    private NoteModel noteModel2;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("TestUser");

        note1 = new Note();
        note1.setId(1L);
        note1.setTitle("Note1");

        note2 = new Note();
        note2.setId(2L);
        note2.setTitle("Note2");

        noteModel1 = new NoteModel();
        noteModel1.setId(1L);
        noteModel1.setTitle("Note1");

        noteModel2 = new NoteModel();
        noteModel2.setId(2L);
        noteModel2.setTitle("Note2");

        user.setNotes(Arrays.asList(note1, note2));
    }

    @Test
    public void toDTO_ValidUser_ReturnsUserModel() {
        when(noteMapper.toDTO(note1)).thenReturn(noteModel1);
        when(noteMapper.toDTO(note2)).thenReturn(noteModel2);

        UserModel userModel = userMapper.toDTO(user);

        assertNotNull(userModel, "UserModel should not be null");
        assertEquals(user.getId(), userModel.getId(), "User ID should match");
        assertEquals(user.getUsername(), userModel.getUsername(), "Username should match");

        List<NoteModel> notes = userModel.getNotes();
        assertNotNull(notes, "Notes should not be null");
        assertEquals(2, notes.size(), "User should have two notes");

        assertEquals(noteModel1.getId(), notes.get(0).getId(), "First note ID should match");
        assertEquals(noteModel2.getId(), notes.get(1).getId(), "Second note ID should match");
    }

    @Test
    public void toDTO_UserWithoutNotes_ReturnsUserModelWithEmptyNotes() {
        user.setNotes(null);

        UserModel userModel = userMapper.toDTO(user);

        assertNotNull(userModel, "UserModel should not be null");
        assertEquals(user.getId(), userModel.getId(), "User ID should match");
        assertEquals(user.getUsername(), userModel.getUsername(), "Username should match");

        assertNull(userModel.getNotes(), "Notes should be null for user with no notes");
    }

    @Test
    public void toEntity_ValidUserModel_ReturnsUser() {
        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setUsername("TestUser");

        User userEntity = userMapper.toEntity(userModel);

        assertNotNull(userEntity, "User entity should not be null");
        assertEquals(userModel.getId(), userEntity.getId(), "User ID should match");
        assertEquals(userModel.getUsername(), userEntity.getUsername(), "Username should match");
    }
}
