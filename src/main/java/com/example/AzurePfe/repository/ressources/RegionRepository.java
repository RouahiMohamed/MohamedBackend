package com.example.AzurePfe.repository.ressources;
import com.example.AzurePfe.models.ressources.DiskSize;
import com.example.AzurePfe.models.ressources.Region;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RegionRepository extends MongoRepository<Region, String> {
    public String getNameById(String id);
}
