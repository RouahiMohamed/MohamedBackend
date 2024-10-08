package com.example.AzurePfe.repository;

import com.example.AzurePfe.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<String> findEmailByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByEmailAndResetToken(String email, String resetToken);

}