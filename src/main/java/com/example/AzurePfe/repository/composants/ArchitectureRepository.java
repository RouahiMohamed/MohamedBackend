package com.example.AzurePfe.repository.composants;

import com.example.AzurePfe.models.User;
import com.example.AzurePfe.models.composant.Architecture;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArchitectureRepository extends MongoRepository<Architecture, String> {
    List<Architecture> findByUser(String user);
}
