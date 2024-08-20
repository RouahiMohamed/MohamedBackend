package com.example.AzurePfe.security.services.composants;

import com.example.AzurePfe.models.composant.ApplicationGateway;
import com.example.AzurePfe.models.composant.ResourceGroup;
import com.example.AzurePfe.models.composant.Subnet;
import com.example.AzurePfe.repository.composants.ApplicationGatewayRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class ApplicationGatewayService {
    @Autowired
    private  ResourceGroupService resourceGroupService;
    @Autowired
    private ApplicationGatewayRepository applicationGatewayRepository;
    @Autowired
    private SubnetService subnetService;
    public ApplicationGateway createApplicationGateway(ApplicationGateway applicationGateway) {
        ResourceGroup resourceGroup = applicationGateway.getResourceGroupe();
        if (resourceGroup != null) {
            resourceGroupService.createResourceGroup(resourceGroup);
        }
        Subnet subnet = applicationGateway.getSubnet();
        if (subnet != null) {
            subnetService.createSubnet(subnet);
        }
        return applicationGatewayRepository.save(applicationGateway);
    }


    public ApplicationGateway getApplicationGatewayById(String id) {
        return applicationGatewayRepository.findById(id).orElse(null);
    }

    public List<ApplicationGateway> getAllApplicationGateways() {
        return applicationGatewayRepository.findAll();
    }

    public boolean deleteApplicationGateway(String id) {
        ApplicationGateway existingApplicationGateway = applicationGatewayRepository.findById(id).orElse(null);
        if (existingApplicationGateway != null) {
            applicationGatewayRepository.delete(existingApplicationGateway);
            return true;
        }
        return false;
    }

    // Additional methods can be implemented here
}
