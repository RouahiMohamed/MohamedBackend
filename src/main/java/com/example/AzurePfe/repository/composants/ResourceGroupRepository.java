package com.example.AzurePfe.repository.composants;

import com.example.AzurePfe.models.composant.ResourceGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceGroupRepository extends MongoRepository<ResourceGroup, String> {

}