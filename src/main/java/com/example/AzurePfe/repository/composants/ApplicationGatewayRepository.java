package com.example.AzurePfe.repository.composants;

import com.example.AzurePfe.models.composant.ApplicationGateway;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicationGatewayRepository extends MongoRepository<ApplicationGateway, String> {
}
