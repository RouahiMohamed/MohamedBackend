package com.example.AzurePfe.repository.ressources;

import com.example.AzurePfe.models.ressources.VMImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VMImageRepository extends MongoRepository<VMImage, String> {
}
