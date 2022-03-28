package com.dustin.pncpoc.repositories;

import com.dustin.pncpoc.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    void deleteUserById(String userId);
}
