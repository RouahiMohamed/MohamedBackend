package com.example.AzurePfe.repository;

import com.example.AzurePfe.models.ERole;
import com.example.AzurePfe.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
