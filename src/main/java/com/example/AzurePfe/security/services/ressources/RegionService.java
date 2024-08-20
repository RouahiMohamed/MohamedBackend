package com.example.AzurePfe.security.services.ressources;

import com.example.AzurePfe.models.ressources.Region;
import com.example.AzurePfe.repository.ressources.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    public Region getRegionById(String id) {
        return regionRepository.findById(id).orElse(null);
    }

    public Region saveRegion(Region Region) {
        return regionRepository.save(Region);
    }

    public void deleteRegionById(String id) {
        regionRepository.deleteById(id);
    }
    public Region updateRegion(String id, Region updatedRegion) {
        Region existingRegion = regionRepository.findById(id).orElse(null);
        if (existingRegion != null) {
            existingRegion.setDisplayName(updatedRegion.getDisplayName());
            existingRegion.setName(updatedRegion.getName());
            existingRegion.setRegionalDisplayName(updatedRegion.getRegionalDisplayName());

            return regionRepository.save(existingRegion);
        }
        return null;
    }
    public String getNameById(String id) {
        return regionRepository.getNameById(id);
    }

}
