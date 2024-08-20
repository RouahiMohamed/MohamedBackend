package com.example.AzurePfe.controllers.composants;

import com.example.AzurePfe.models.composant.ResourceGroup;
import com.example.AzurePfe.security.services.composants.ResourceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@CrossOrigin(origins = "http://40.83.171.29", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/resourceGroups")
public class ResourceGroupController {
    @Autowired
    private ResourceGroupService resourceGroupService;

    @PostMapping("/add")
    public ResourceGroup createResourceGroup(@RequestBody ResourceGroup resourceGroup) {
        return resourceGroupService.createResourceGroup(resourceGroup);
    }

    @GetMapping("/getRessource/{id}")
    public ResourceGroup getResourceGroup(@PathVariable String id) {
        return resourceGroupService.getResourceGroupById(id);
    }
    @PutMapping("/update/{id}")
    public ResourceGroup updateResourceGroup(@PathVariable String id, @RequestBody ResourceGroup updatedResourceGroup) {
        return resourceGroupService.updateResourceGroup(id, updatedResourceGroup);
    }

    @GetMapping("/getAllRessource")
    public List<ResourceGroup> getAllResourceGroups() {
        return resourceGroupService.getAllResourceGroups();
    }

    @DeleteMapping("/{id}")
    public void deleteResourceGroup(@PathVariable String id) {
        resourceGroupService.deleteResourceGroup(id);
    }
}