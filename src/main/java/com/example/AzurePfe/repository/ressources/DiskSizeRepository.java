package com.example.AzurePfe.repository.ressources;

import com.example.AzurePfe.models.ressources.DiskSize;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DiskSizeRepository extends MongoRepository<DiskSize, String> {
    public List<DiskSize> findByIdRegion(String idRegion);
}