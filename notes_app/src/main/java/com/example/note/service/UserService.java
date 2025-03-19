package com.example.note.service;

import com.example.note.dto.UserModel;

import java.util.List;

public interface UserService {

    List<UserModel> getAllUsers();

    UserModel getUser(Long id);

    UserModel createUser(UserModel userModel);

    UserModel updateUser(Long id, UserModel userModel);

    void deleteUser(Long id);
}
