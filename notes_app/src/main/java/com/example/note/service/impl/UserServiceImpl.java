package com.example.note.service.impl;
import com.example.note.dto.UserModel;
import com.example.note.entity.User;
import com.example.note.exception.NotFoundException;
import com.example.note.mapper.UserMapper;
import com.example.note.repository.UserRepository;
import com.example.note.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserModel getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User not found with id %s", id)));
        return userMapper.toDTO(user);
    }

    @Override
    public UserModel createUser(UserModel userModel) {
        User user = userMapper.toEntity(userModel);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public UserModel updateUser(Long id, UserModel userModel) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUsername(userModel.getUsername());
            existingUser.setPassword(userModel.getPassword());
            userRepository.save(existingUser);
            return userMapper.toDTO(existingUser);
        } else {
            throw new NotFoundException(String.format("User not found with id %s", id));
        }
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException(String.format("User not found with id %s", id));
        }
    }
}
