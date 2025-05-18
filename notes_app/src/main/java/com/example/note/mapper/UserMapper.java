package com.example.note.mapper;

import com.example.note.dto.UserModel;
import com.example.note.entity.Note;
import com.example.note.dto.NoteModel;
import com.example.note.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    NoteMapper noteMapper;

    public UserModel toDTO(User user) {
        UserModel userModel = new UserModel();
        userModel.setId(user.getId());
        userModel.setUsername(user.getUsername());
        userModel.setPassword(user.getPassword());
        userModel.setNotes(convertToNoteModelList(user.getNotes()));
        return userModel;
    }

    private List<NoteModel> convertToNoteModelList(List<Note> notes) {
        if (notes == null) {
            return null;
        }
        return notes.stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public User toEntity(UserModel dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }
}
