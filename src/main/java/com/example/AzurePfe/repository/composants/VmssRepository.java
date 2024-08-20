package com.example.AzurePfe.repository.composants;

import com.example.AzurePfe.models.composant.Vmss;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VmssRepository extends MongoRepository<Vmss, String> {
}
