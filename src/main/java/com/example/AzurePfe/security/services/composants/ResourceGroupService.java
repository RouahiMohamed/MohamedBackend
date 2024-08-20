package com.example.AzurePfe.security.services.composants;

import com.example.AzurePfe.models.composant.ResourceGroup;
import com.example.AzurePfe.repository.composants.ResourceGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class ResourceGroupService {

    @Autowired
    private ResourceGroupRepository resourceGroupRepository;

    public ResourceGroup createResourceGroup(ResourceGroup resourceGroup) {
        return resourceGroupRepository.save(resourceGroup);
    }

    public ResourceGroup getResourceGroupById(String id) {
        return resourceGroupRepository.findById(id).orElse(null);
    }

    public List<ResourceGroup> getAllResourceGroups() {
        return resourceGroupRepository.findAll();
    }

    public ResourceGroup updateResourceGroup(String id, ResourceGroup updatedResourceGroup) {
        ResourceGroup existingResourceGroup = resourceGroupRepository.findById(id).orElse(null);
        if (existingResourceGroup != null) {
            // Update fields of existingResourceGroup with fields of updatedResourceGroup
            existingResourceGroup.setName(updatedResourceGroup.getName());
                existingResourceGroup.setRegion(updatedResourceGroup.getRegion());


            // Save the updated resource group
            return resourceGroupRepository.save(existingResourceGroup);
        }
        return null;
    }

    public boolean deleteResourceGroup(String id) {
        ResourceGroup existingResourceGroup = resourceGroupRepository.findById(id).orElse(null);
        if (existingResourceGroup != null) {
            resourceGroupRepository.delete(existingResourceGroup);
            return true;
        }
        return false;
    }
}
