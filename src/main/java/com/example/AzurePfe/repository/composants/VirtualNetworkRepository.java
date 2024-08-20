package com.example.AzurePfe.repository.composants;

import com.example.AzurePfe.models.composant.VirtualNetwork;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VirtualNetworkRepository extends MongoRepository<VirtualNetwork, String> {
}
