package com.example.AzurePfe.security.services.composants;

import com.example.AzurePfe.models.User;
import com.example.AzurePfe.models.composant.*;
import com.example.AzurePfe.repository.UserRepository;
import com.example.AzurePfe.repository.composants.ArchitectureRepository;
import com.example.AzurePfe.security.services.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArchitectureService {

    @Autowired
    private ArchitectureRepository architectureRepository;
    @Autowired
    private  ResourceGroupService createResourceGroup;
    @Autowired
    private SubnetService subnetService;
    @Autowired
    private VirtualMachineService virtualMachineService;
    @Autowired
    private VirtualNetworkService virtualNetworkService;
    @Autowired
    private VmssService vmssService;

    @Autowired
    private ApplicationGatewayService  applicationGatewayService;

    @Autowired
    private UserRepository userRepository;

    public Architecture createArchitecture(Architecture architecture) {
        for (ResourceGroup resourceGroup : architecture.getResourceGroups()) {
             createResourceGroup.createResourceGroup(resourceGroup);
        }
        for (VirtualMachine resourceGroup : architecture.getVirtualMachines()) {
            virtualMachineService.saveVirtualMachine(resourceGroup);
        }
        for (VirtualNetwork resourceGroup : architecture.getVirtualNetworks()) {
            virtualNetworkService.createVirtualNetwork(resourceGroup);
        }
        for (Vmss resourceGroup : architecture.getVmsses()) {
            vmssService.createVmss(resourceGroup);
        }
        for (Subnet resourceGroup : architecture.getSubnets()) {
            subnetService.createSubnet(resourceGroup);
        }
        for (ApplicationGateway resourceGroup : architecture.getApplicationGateways()) {
            applicationGatewayService.createApplicationGateway(resourceGroup);
        }
        return architectureRepository.save(architecture);
    }
    public Architecture updateArchitecture(String id, Architecture updatedArchitecture) {
        Optional<Architecture> existingArchitectureOptional = architectureRepository.findById(id);
        if (existingArchitectureOptional.isPresent()) {
            Architecture existingArchitecture = existingArchitectureOptional.get();
            existingArchitecture.setName(updatedArchitecture.getName());
            existingArchitecture.setDateCreation(updatedArchitecture.getDateCreation());
            existingArchitecture.setResourceGroups(updatedArchitecture.getResourceGroups());
            existingArchitecture.setVmsses(updatedArchitecture.getVmsses());
            existingArchitecture.setVirtualMachines(updatedArchitecture.getVirtualMachines());
            existingArchitecture.setVirtualNetworks(updatedArchitecture.getVirtualNetworks());
            existingArchitecture.setApplicationGateways(updatedArchitecture.getApplicationGateways());
            existingArchitecture.setSubnets(updatedArchitecture.getSubnets());
            for (ResourceGroup resourceGroup : updatedArchitecture.getResourceGroups()) {
                createResourceGroup.createResourceGroup(resourceGroup);
            }
            for (VirtualMachine resourceGroup : updatedArchitecture.getVirtualMachines()) {
                virtualMachineService.saveVirtualMachine(resourceGroup);
            }
            for (VirtualNetwork resourceGroup : updatedArchitecture.getVirtualNetworks()) {
                virtualNetworkService.createVirtualNetwork(resourceGroup);
            }
            for (Vmss resourceGroup : updatedArchitecture.getVmsses()) {
                vmssService.createVmss(resourceGroup);
            }
            for (Subnet resourceGroup : updatedArchitecture.getSubnets()) {
                subnetService.createSubnet(resourceGroup);
            }
            for (ApplicationGateway resourceGroup : updatedArchitecture.getApplicationGateways()) {
                applicationGatewayService.createApplicationGateway(resourceGroup);
            }
            return architectureRepository.save(existingArchitecture);
        } else {
            return null;
        }
    }


    public List<Architecture> getAllArchitectures() {
        return architectureRepository.findAll();
    }

    public Architecture getArchitectureById(String id) {
        return architectureRepository.findById(id).orElse(null);
    }

    public Architecture saveCostEstimation(String architectureId, String costEstimation) {
        Optional<Architecture> architectureOptional = architectureRepository.findById(architectureId);
        if (architectureOptional.isPresent()) {
            Architecture architecture = architectureOptional.get();
            architecture.setCostEstimation(costEstimation);
            return architectureRepository.save(architecture);
        } else {
            throw new RuntimeException("Architecture not found with id: " + architectureId);
        }
    }
    public List<Architecture> getArchitecturesByUser(String user) {
        return architectureRepository.findByUser(user);
    }
    public void deleteArchitecture(String id) {
        architectureRepository.deleteById(id);
    }
}
