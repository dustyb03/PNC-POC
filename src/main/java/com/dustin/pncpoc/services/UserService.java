package com.dustin.pncpoc.services;

import com.dustin.pncpoc.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User newUser);
    User updateUser(User user, String id);
    List<User> getAllUsers();
    void deleteUser(String userId);
    Optional<User> findByUserId(String id);
}
