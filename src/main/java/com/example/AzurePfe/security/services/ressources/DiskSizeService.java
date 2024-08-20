package com.example.AzurePfe.security.services.ressources;

import com.example.AzurePfe.models.ressources.DiskSize;
import com.example.AzurePfe.repository.ressources.DiskSizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiskSizeService {
    @Autowired
    private DiskSizeRepository diskSizeRepository;

    public List<DiskSize> getAllDiskSizes() {
        return diskSizeRepository.findAll();
    }

    public List<DiskSize> getByIdRegion(String idRegion) {
        return diskSizeRepository.findByIdRegion(idRegion);
    }
    public DiskSize getDiskSizeById(String id) {
        return diskSizeRepository.findById(id).orElse(null);
    }

    public DiskSize saveDiskSize(DiskSize diskSize) {
        return diskSizeRepository.save(diskSize);
    }

    public void deleteDiskSizeById(String id) {
        diskSizeRepository.deleteById(id);
    }
    public DiskSize updateDiskSize(String id, DiskSize updatedDiskSize) {
        DiskSize existingDiskSize = diskSizeRepository.findById(id).orElse(null);
        if (existingDiskSize != null) {
            // Updating the fields of the existing DiskSize
            existingDiskSize.setMaxDataDiskCount(updatedDiskSize.getMaxDataDiskCount());
            existingDiskSize.setIdRegion(updatedDiskSize.getIdRegion());
            existingDiskSize.setMemoryInMB(updatedDiskSize.getMemoryInMB());
            existingDiskSize.setName(updatedDiskSize.getName());
            existingDiskSize.setNumberOfCores(updatedDiskSize.getNumberOfCores());
            existingDiskSize.setOsDiskSizeInMB(updatedDiskSize.getOsDiskSizeInMB());
            existingDiskSize.setResourceDiskSizeInMB(updatedDiskSize.getResourceDiskSizeInMB());

            // Save the updated entity to the repository
            return diskSizeRepository.save(existingDiskSize);
        }
        return null;
    }
}
