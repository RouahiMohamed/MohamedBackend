package com.example.AzurePfe.repository.composants;

import com.example.AzurePfe.models.composant.Subnet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubnetRepository extends MongoRepository<Subnet, String> {
}
