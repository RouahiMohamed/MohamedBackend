package com.example.AzurePfe.security.services.composants;



import com.example.AzurePfe.models.composant.ResourceGroup;
import com.example.AzurePfe.models.composant.VirtualNetwork;
import com.example.AzurePfe.repository.composants.VirtualNetworkRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class VirtualNetworkService {
    @Autowired
    private  ResourceGroupService resourceGroupService;
    @Autowired
    private VirtualNetworkRepository virtualNetworkRepository;

    public VirtualNetwork createVirtualNetwork(VirtualNetwork virtualNetwork) {
        ResourceGroup resourceGroup = virtualNetwork.getResourceGroup();
        if (resourceGroup != null) {
            resourceGroupService.createResourceGroup(resourceGroup);
        }
        return virtualNetworkRepository.save(virtualNetwork);
    }

    public VirtualNetwork getVirtualNetworkById(String id) {
        return virtualNetworkRepository.findById(id).orElse(null);
    }

    public List<VirtualNetwork> getAllVirtualNetworks() {
        return virtualNetworkRepository.findAll();
    }

    public boolean deleteVirtualNetwork(String id) {
        VirtualNetwork existingVirtualNetwork = virtualNetworkRepository.findById(id).orElse(null);
        if (existingVirtualNetwork != null) {
            virtualNetworkRepository.delete(existingVirtualNetwork);
            return true;
        }
        return false;
    }

}
