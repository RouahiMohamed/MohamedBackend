package com.example.AzurePfe.security.services.composants;

import com.example.AzurePfe.models.composant.ResourceGroup;
import com.example.AzurePfe.models.composant.Subnet;
import com.example.AzurePfe.models.composant.VirtualMachine;
import com.example.AzurePfe.repository.composants.ApplicationGatewayRepository;
import com.example.AzurePfe.repository.composants.VirtualMachineRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VirtualMachineService {
    @Autowired
    private  ResourceGroupService resourceGroupService;

    @Autowired
    private SubnetService subnetService;
    @Autowired
    private VirtualMachineRepository virtualMachineRepository;

    public List<VirtualMachine> getAllVirtualMachines() {
        return virtualMachineRepository.findAll();
    }

    public Optional<VirtualMachine> getVirtualMachineById(String id) {
        return virtualMachineRepository.findById(id);
    }

    public VirtualMachine saveVirtualMachine(VirtualMachine virtualMachine) {
        ResourceGroup resourceGroup = virtualMachine.getResourceGroupe();
        if (resourceGroup != null) {
            resourceGroupService.createResourceGroup(resourceGroup);
        }
        Subnet subnet = virtualMachine.getSubnet();
        if (subnet != null) {
            subnetService.createSubnet(subnet);
        }
        return virtualMachineRepository.save(virtualMachine);
    }

    public void deleteVirtualMachineById(String id) {
        virtualMachineRepository.deleteById(id);
    }
}
