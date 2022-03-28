package com.dustin.pncpoc.services;

import com.dustin.pncpoc.models.User;
import com.dustin.pncpoc.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User newUser) {
        return userRepository.save(newUser);
    }


    @Override
    public User updateUser(User user, String id) {
        Optional<User> existingUser = this.findByUserId(id);
        if (existingUser.isPresent()) {
            return this.userRepository.save(user);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }


    @Override
    public Optional<User> findByUserId(String id) {
        return this.userRepository.findById(id);
    }

    @Override
    public void deleteUser(String userId) {
        this.userRepository.deleteUserById(userId);
    }
}
