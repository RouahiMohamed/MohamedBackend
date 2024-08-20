package com.example.AzurePfe.controllers.composants;

import com.example.AzurePfe.models.User;
import com.example.AzurePfe.models.composant.*;
import com.example.AzurePfe.models.ressources.Region;
import com.example.AzurePfe.repository.composants.ArchitectureRepository;
import com.example.AzurePfe.security.services.composants.ArchitectureService;
import com.example.AzurePfe.security.services.infracost.InfracostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@CrossOrigin(origins = "http://40.83.171.29", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/architectures")

public class ArchitectureController {

    @Autowired
    private ArchitectureService architectureService;
    @Autowired
    private ArchitectureRepository architectureRepository;
    @Autowired
    private InfracostService infracostService;


    @PostMapping("/estimateCost")
    public Map<String, String> estimateCost(@RequestBody Map<String, String> request) throws IOException, InterruptedException {
        String terraformCode = request.get("terraformCode");
        return infracostService.estimateCosts(terraformCode);
    }

    @PostMapping("/saveCostEstimation/{id}")
    public ResponseEntity<Architecture> saveCostEstimation(@PathVariable String id, @RequestBody Map<String, String> request) {
        String costEstimation = request.get("costEstimation");
        Architecture architecture = architectureService.saveCostEstimation(id, costEstimation);
        return ResponseEntity.ok(architecture);
    }


    @GetMapping("/getAll")
    public List<Architecture> getAllArchitectures() {
        return architectureService.getAllArchitectures();
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Architecture> updateArchitecture(@PathVariable String id, @RequestBody Architecture updatedArchitecture) {

        Architecture updated = architectureService.updateArchitecture(id, updatedArchitecture);
        return ResponseEntity.ok(updated);}
    @GetMapping("/getById/{id}")
    public Architecture getArchitectureById(@PathVariable String id) {
        return architectureService.getArchitectureById(id);
    }

    @PostMapping("/add")
    public Architecture createArchitecture(@RequestBody Architecture architecture) {

        return architectureService.createArchitecture(architecture);
    }
    @PostMapping("/generate-terraform-code")
    public String generateTerraformCode(@RequestBody Architecture architecture) {
        StringBuilder terraformCode = new StringBuilder();
        terraformCode.append("provider \"azurerm\" {\n")
                .append("  features {}\n")
                .append("}\n\n");

        // Générer le code pour les groupes de ressources
        for (ResourceGroup resourceGroup : architecture.getResourceGroups()) {
            terraformCode.append("resource \"azurerm_resource_group\" \"").append(resourceGroup.getName()).append("\" {\n")
                    .append("  name     = \"").append(resourceGroup.getName()).append("\"\n")
                    .append("  location = \"").append(resourceGroup.getRegion().getName()).append("\"\n")
                    .append("}\n\n");
        }

        // Générer le code pour les réseaux virtuels
        for (VirtualNetwork virtualNetwork : architecture.getVirtualNetworks()) {
            terraformCode.append("resource \"azurerm_virtual_network\" \"").append(virtualNetwork.getName()).append("\" {\n")
                    .append("  name                = \"").append(virtualNetwork.getName()).append("\"\n");
            if (virtualNetwork.getResourceGroup() != null){
                terraformCode.append("  resource_group_name = azurerm_resource_group.").append(virtualNetwork.getResourceGroup().getName()).append(".name\n");}
            terraformCode.append("  location            = azurerm_resource_group.").append(virtualNetwork.getResourceGroup().getName()).append(".location\n")
                    .append("  address_space       = [\"").append(virtualNetwork.getIpAddresses()).append("\"]\n")
                    .append("}\n\n");
        }

        // Générer le code pour les sous-réseaux
        for (Subnet subnet : architecture.getSubnets()) {
            terraformCode.append("resource \"azurerm_subnet\" \"").append(subnet.getName()).append("\" {\n")
                    .append("  name                 = \"").append(subnet.getName()).append("\"\n")
                    .append("  address_prefixes     = [\"").append(subnet.getAdress()).append("\"]\n");
            if (subnet.getResourceGroupe() != null){
                terraformCode.append("  resource_group_name = azurerm_resource_group.").append(subnet.getResourceGroupe().getName()).append(".name\n");}
            if (subnet.getVirtualNetworks() != null && !subnet.getVirtualNetworks().isEmpty()) {
                for (VirtualNetwork vn : subnet.getVirtualNetworks()) {

                    terraformCode .append("  virtual_network_name = azurerm_virtual_network.").append(vn.getName()).append(".name\n");
                }
            }
            terraformCode.append("}\n\n");
            terraformCode.append("resource \"azurerm_network_interface\" \"").append(subnet.getName()).append("-nic\" {\n")
                    .append("  name                = \"").append(subnet.getName()).append("-nic\"\n")
                    .append("  location            = azurerm_resource_group.").append(subnet.getResourceGroupe().getName()).append(".location\n")
                    .append("  resource_group_name = azurerm_resource_group.").append(subnet.getResourceGroupe().getName()).append(".name\n")
                    .append("  ip_configuration {\n")
                    .append("    name                          = \"internal\"\n")
                    .append("    subnet_id                     = azurerm_subnet.").append(subnet.getName()).append(".id\n")
                    .append("    private_ip_address_allocation = \"Dynamic\"\n")
                    .append("  }\n")
                    .append("}\n\n");

        }


        // Générer le code pour les app gateways
        for (ApplicationGateway appGateway : architecture.getApplicationGateways()) {
            if (appGateway.getSubnet() != null) {
                // Incrémenter l'adresse du sous-réseau de l'Application Gateway
                String originalAddress = appGateway.getSubnet().getAdress(); // Suppose que l'adresse est au format "192.168.1.0/24"
                String[] addressParts = originalAddress.split("\\.");
                int lastSegment = Integer.parseInt(addressParts[2]) + 1; // Incrémenter le troisième octet
                String newAddress = addressParts[0] + "." + addressParts[1] + "." + lastSegment + ".0/24";

                terraformCode.append("resource \"azurerm_subnet\" \"").append(appGateway.getName()).append("-subnet\" {\n")
                        .append("  name                 = \"").append(appGateway.getName()).append("-subnet\"\n")
                        .append("  address_prefixes     = [\"").append(newAddress).append("\"]\n")
                        .append("  resource_group_name  = azurerm_resource_group.").append(appGateway.getResourceGroupe().getName()).append(".name\n");
                if (appGateway.getSubnet().getVirtualNetworks() != null && !appGateway.getSubnet().getVirtualNetworks().isEmpty()) {
                    for (VirtualNetwork vn : appGateway.getSubnet().getVirtualNetworks()) {
                        terraformCode.append("  virtual_network_name = azurerm_virtual_network.").append(vn.getName()).append(".name\n");
                    }
                }
                terraformCode.append("}\n\n");
            }


            // Add azurerm_public_ip block
            terraformCode.append("resource \"azurerm_public_ip\" \"")
                    .append(appGateway.getName()).append("PublicIP\" {\n")
                    .append("  name                = \"").append(appGateway.getName()).append("PublicIP\"\n")
                    .append("  location            = azurerm_resource_group.")
                    .append(appGateway.getResourceGroupe().getName()).append(".location\n")
                    .append("  resource_group_name = azurerm_resource_group.")
                    .append(appGateway.getResourceGroupe().getName()).append(".name\n")
                    .append("  allocation_method   = \"Static\"\n")
                    .append("  sku                 = \"Standard\"\n")
                    .append("}\n\n");

            // Add azurerm_application_gateway block
            terraformCode.append("resource \"azurerm_application_gateway\" \"").append(appGateway.getName()).append("\" {\n")
                    .append("  name                = \"").append(appGateway.getName()).append("\"\n");

            if (appGateway.getResourceGroupe() != null) {
                terraformCode.append("  resource_group_name = azurerm_resource_group.")
                        .append(appGateway.getResourceGroupe().getName()).append(".name\n");
            }

            terraformCode.append("  location            = azurerm_resource_group.")
                    .append(appGateway.getResourceGroupe().getName()).append(".location\n");

            if (appGateway.getSubnet() != null) {
                terraformCode.append("  gateway_ip_configuration {\n")
                        .append("    name      = \"").append(appGateway.getNameGatewayIpConfiguration()).append("\"\n")
                        .append("    subnet_id = azurerm_subnet.").append(appGateway.getName()).append("-subnet.id\n")
                        .append("  }\n");
            }

            terraformCode.append("  sku {\n")
                    .append("    name     = \"Standard_v2\"\n")
                    .append("    tier     = \"Standard_v2\"\n")
                    .append("    capacity = 2\n")
                    .append("  }\n");

            terraformCode.append("  frontend_port {\n")
                    .append("    name = \"").append(appGateway.getNameFrontendPort()).append("\"\n")
                    .append("    port = 80\n")
                    .append("  }\n");

            terraformCode.append("  frontend_ip_configuration {\n")
                    .append("    name                 = \"").append(appGateway.getNameFrontendIpconfiguration()).append("\"\n")
                    .append("    public_ip_address_id = azurerm_public_ip.")
                    .append(appGateway.getName()).append("PublicIP.id\n")
                    .append("  }\n");

            terraformCode.append("  backend_address_pool {\n")
                    .append("    name = \"").append(appGateway.getNamebackendAddressPool()).append("\"\n")
                    .append("  }\n");

            terraformCode.append("  backend_http_settings {\n")
                    .append("    name                  = \"").append(appGateway.getNameBackendHttpSettings()).append("\"\n")
                    .append("    cookie_based_affinity = \"Disabled\"\n")
                    .append("    port                  = 80\n")
                    .append("    protocol              = \"Http\"\n")
                    .append("    request_timeout       = 20\n")
                    .append("  }\n");

            terraformCode.append("  http_listener {\n")
                    .append("    name                           = \"").append(appGateway.getNameHttpListener()).append("\"\n")
                    .append("    frontend_ip_configuration_name = \"").append(appGateway.getNameFrontendIpconfiguration()).append("\"\n")
                    .append("    frontend_port_name             = \"").append(appGateway.getNameFrontendPort()).append("\"\n")
                    .append("    protocol                       = \"Http\"\n")
                    .append("  }\n");

            terraformCode.append("  request_routing_rule {\n")
                    .append("    name                       = \"").append(appGateway.getNameRequestRoutingRule()).append("\"\n")
                    .append("    rule_type                  = \"Basic\"\n")
                    .append("    http_listener_name         = \"").append(appGateway.getNameHttpListener()).append("\"\n")
                    .append("    backend_address_pool_name  = \"").append(appGateway.getNamebackendAddressPool()).append("\"\n")
                    .append("    backend_http_settings_name = \"").append(appGateway.getNameBackendHttpSettings()).append("\"\n")
                    .append("    priority                   = 100\n")
                    .append("  }\n");

            terraformCode.append("}\n\n");
        }


        // Générer le code pour les machines virtuelles
        for (VirtualMachine vm : architecture.getVirtualMachines()) {
            terraformCode.append("resource \"azurerm_virtual_machine\" \"").append(vm.getName()).append("\" {\n")
                    .append("  name                  = \"").append(vm.getName()).append("\"\n");
            if (vm.getResourceGroupe() != null){
                terraformCode.append("  resource_group_name   = azurerm_resource_group.").append(vm.getResourceGroupe().getName()).append(".name\n");
            }
            terraformCode.append("  location              = azurerm_resource_group.").append(vm.getResourceGroupe().getName()).append(".location\n")
                    .append("  vm_size               = \"").append(vm.getIdDiskSize().getName()).append("\"\n");
            if (vm.getSubnet() != null){
                terraformCode.append("  network_interface_ids = [azurerm_network_interface.").append(vm.getSubnet().getName()).append("-nic.id]\n");
            }

            terraformCode.append("  os_profile {\n")
                    .append("    computer_name  = \"").append(vm.getName()).append("\"\n")
                    .append("    admin_username = \"").append(vm.getUsername()).append("\"\n")
                    .append("    admin_password = \"").append(vm.getPassword()).append("\"\n")
                    .append("  }\n")
                    .append("  os_profile_linux_config {\n")
                    .append("    disable_password_authentication = false\n")
                    .append("  }\n")
                    .append("  storage_os_disk {\n")
                    .append("    name              = \"").append(vm.getName()).append("_os_disk\"\n")
                    .append("    caching           = \"ReadWrite\"\n")
                    .append("    create_option     = \"FromImage\"\n")
                    .append("    disk_size_gb      = ").append(vm.getIdDiskSize().getOsDiskSizeInMB() / 1024).append("\n")
                    .append("  }\n")
                    .append("  storage_image_reference {\n")
                    .append("    publisher = \"").append(vm.getIdImage().getPublisher()).append("\"\n")
                    .append("    offer     = \"").append(vm.getIdImage().getOffer()).append("\"\n")
                    .append("    sku       = \"").append(vm.getIdImage().getSku()).append("\"\n")
                    .append("    version   = \"").append(vm.getIdImage().getVersion()).append("\"\n")
                    .append("  }\n")
                    .append("}\n\n");
        }


// Générer le code pour les ensembles de machines virtuelles (VMSS)
        for (Vmss vmss : architecture.getVmsses()) {
            terraformCode.append("resource \"azurerm_linux_virtual_machine_scale_set\" \"").append(vmss.getName()).append("\" {\n")
                    .append("  name                = \"").append(vmss.getName()).append("\"\n")
                    .append("  resource_group_name = azurerm_resource_group.").append(vmss.getVirtualMachine().getResourceGroupe().getName()).append(".name\n")
                    .append("  location            = \"").append(vmss.getVirtualMachine().getRegion().getName()).append("\"\n")
                    .append("  sku {\n")
                    .append("    capacity = ").append(vmss.getNb_vm()).append("\n")
                    .append("    tier     = \"Standard\"\n")
                    .append("    size     = \"").append(vmss.getVirtualMachine().getIdDiskSize().getName()).append("\"\n")
                    .append("  }\n")
                    .append("  upgrade_policy {\n")
                    .append("    mode = \"Automatic\"\n")
                    .append("  }\n")
                    .append("  os_profile {\n")
                    .append("    computer_name_prefix = \"").append(vmss.getName()).append("\"\n")
                    .append("    admin_username       = \"").append(vmss.getVirtualMachine().getUsername()).append("\"\n")
                    .append("    admin_password       = \"").append(vmss.getVirtualMachine().getPassword()).append("\"\n")
                    .append("  }\n")
                    .append("  os_profile_linux_config {\n")
                    .append("    disable_password_authentication = false\n")
                    .append("  }\n")
                    .append("  source_image_reference {\n")
                    .append("    publisher = \"").append(vmss.getVirtualMachine().getIdImage().getPublisher()).append("\"\n")
                    .append("    offer     = \"").append(vmss.getVirtualMachine().getIdImage().getOffer()).append("\"\n")
                    .append("    sku       = \"").append(vmss.getVirtualMachine().getIdImage().getSku()).append("\"\n")
                    .append("    version   = \"").append(vmss.getVirtualMachine().getIdImage().getVersion()).append("\"\n")
                    .append("  }\n")
                    .append("  network_profile {\n")
                    .append("    name    = \"").append(vmss.getName()).append("_network_profile\"\n")
                    .append("    primary = true\n")
                    .append("    ip_configuration {\n")
                    .append("      name      = \"").append(vmss.getName()).append("_ip_config\"\n")
                    .append("      subnet_id = azurerm_subnet.").append(vmss.getVirtualMachine().getSubnet().getName()).append(".id\n")
                    .append("    }\n")
                    .append("  }\n")
                    .append("}\n\n");
        }


        architecture.setTerraformCode(terraformCode.toString());
        architectureRepository.save(architecture);
        return terraformCode.toString();
    }


    @PostMapping("/generate-pulumi-code")
    public String generatePulumiCode(@RequestBody Architecture architecture) {
        StringBuilder pulumiCode = new StringBuilder();
        pulumiCode.append("import * as pulumi from '@pulumi/pulumi';\n");
        pulumiCode.append("import * as azure from '@pulumi/azure';\n\n");
        // Générer le code pour le groupe de ressources
        for (ResourceGroup resourceGroup : architecture.getResourceGroups()) {
            pulumiCode.append("const ").append(resourceGroup.getName()).append(" = new azure.core.ResourceGroup(\"")
                    .append(resourceGroup.getName()).append("\", {\n")
                    .append("    name: \"").append(resourceGroup.getName()).append("\",\n")
                    .append("    location: \"").append(resourceGroup.getRegion().getName()).append("\",\n")
                    .append("});\n\n");
        }

        for (VirtualNetwork virtualNetwork : architecture.getVirtualNetworks()) {
            pulumiCode.append("const ").append(virtualNetwork.getName()).append(" = new azure.network.VirtualNetwork(\"")
                    .append(virtualNetwork.getName()).append("\", {\n")
                    .append("    name: \"").append(virtualNetwork.getName()).append("\",\n");
            if (virtualNetwork.getResourceGroup() != null){
                pulumiCode.append("    resourceGroupName: \"").
                        append(virtualNetwork.getResourceGroup().getName()).append("\",\n");}
            pulumiCode.append("    location: \"").append(virtualNetwork.getResourceGroup().getRegion().getName()).append("\",\n")
                    .append("    addressSpaces: [\"").append(virtualNetwork.getIpAddresses()).append("\"],\n")
                    .append("});\n");
        }

        //SUBNETTT
        for (Subnet subnet : architecture.getSubnets()) {
            pulumiCode.append("const ").append(subnet.getName()).append(" = new azure.network.Subnet('")
                    .append(subnet.getName()).append("', {\n")
                    .append("    name: \"").append(subnet.getName()).append("\",\n")
                    .append("    addressPrefix: ['").append(subnet.getAdress()).append("'],\n");
            if (subnet.getResourceGroupe() != null) {
                pulumiCode.append("    resourceGroupName: \"").append(subnet.getResourceGroupe().getName()).append("\",\n");
            }
            if (subnet.getVirtualNetworks() != null && !subnet.getVirtualNetworks().isEmpty()) {
                for (VirtualNetwork vn : subnet.getVirtualNetworks()) {
                    pulumiCode.append("    virtualNetworkName: \"").append(vn.getName()).append("\",\n");
                }
            }
            pulumiCode.append("});\n");
        }


//APPLICATIONgATEWAY
        // APPLICATION_GATEWAY
        for (ApplicationGateway appGateway : architecture.getApplicationGateways()) {
            pulumiCode.append("const ").append(appGateway.getName()).append(" = new azure.network.ApplicationGateway('")
                    .append(appGateway.getName()).append("', {\n")
                    .append("    name: \"").append(appGateway.getName()).append("\",\n")
                    .append("    location: \"").append(appGateway.getResourceGroupe().getRegion().getName()).append("\"\n");

            if (appGateway.getResourceGroupe() != null) {
                pulumiCode.append("    resourceGroupName: \"").append(appGateway.getResourceGroupe().getName()).append("\",\n");
            }
            if (appGateway.getSubnet() != null) {
                pulumiCode.append("    gatewayIpConfigurations: [{\n")
                        .append("        name: 'my-gateway-ip-configuration',\n")
                        .append("        subnetId: ").append(appGateway.getSubnet().getId()).append("\n")
                        .append("    }],\n");
            }
            pulumiCode.append("});\n");
        }


        // vm
        // VIRTUAL_MACHINE
        for (VirtualMachine vm : architecture.getVirtualMachines()) {
            pulumiCode.append("const ").append(vm.getName()).append(" = new azure.compute.VirtualMachine('").append(vm.getName()).append("', {\n")
                    .append("    name: \"").append(vm.getName()).append("\",\n");
            if (vm.getResourceGroupe() != null) {
                pulumiCode.append("    resourceGroupName: ").append(vm.getResourceGroupe().getName()).append(".name,\n");
            }
            pulumiCode.append("    location: ").append(vm.getRegion().getName()).append(".location,\n")
                    .append("    vmSize: '").append(vm.getIdDiskSize().getName()).append("',\n")
                    .append("    osProfile: {\n")
                    .append("        computerName: \"").append(vm.getName()).append("\",\n")
                    .append("        adminUsername: \"").append(vm.getUsername()).append("\",\n")
                    .append("        adminPassword: pulumi.secret(\"").append(vm.getPassword()).append("\"),\n")
                    .append("    },\n")
                    .append("    osProfileLinuxConfig: {\n")
                    .append("        disablePasswordAuthentication: false,\n")
                    .append("    },\n")
                    .append("    storageImageReference: {\n")
                    .append("        publisher: \"").append(vm.getIdImage().getPublisher()).append("\",\n")
                    .append("        offer: \"").append(vm.getIdImage().getOffer()).append("\",\n")
                    .append("        sku: \"").append(vm.getIdImage().getSku()).append("\",\n")
                    .append("        version: \"").append(vm.getIdImage().getVersion()).append("\",\n")
                    .append("    },\n");

            if (vm.getSubnet() != null) {
                pulumiCode.append("    networkInterfacesIds: [")
                        .append(vm.getSubnet().getName())
                        .append(".id],\n");
            }

            pulumiCode.append("});\n");
        }


        for (Vmss vmss : architecture.getVmsses()) {
            pulumiCode.append("const ").append(vmss.getName()).append(" = new azure.compute.LinuxVirtualMachineScaleSet('").append(vmss.getName()).append("', {\n")
                    .append("    name: \"").append(vmss.getName()).append("\",\n")
                    .append("    resourceGroupName: ").append(vmss.getVirtualMachine().getResourceGroupe().getName()).append(",\n")
                    .append("    location: ").append(vmss.getVirtualMachine().getRegion().getName()).append(",\n")
                    .append("    sku: {\n")
                    .append("        capacity: ").append(vmss.getNb_vm()).append(",\n")
                    .append("        tier: \"Standard\",\n")
                    .append("        size: \"").append(vmss.getVirtualMachine().getIdDiskSize().getName()).append("\",\n")
                    .append("    },\n")
                    .append("    upgradePolicy: {\n")
                    .append("        mode: 'Automatic',\n")
                    .append("    },\n")
                    .append("    osProfile: {\n")
                    .append("        computerNamePrefix: \"").append(vmss.getName()).append("\",\n")
                    .append("        adminUsername: \"").append(vmss.getVirtualMachine().getUsername()).append("\",\n")
                    .append("        adminPassword: pulumi.secret(\"").append(vmss.getVirtualMachine().getPassword()).append("\"),\n")
                    .append("    },\n")
                    .append("    osProfileLinuxConfig: {\n")
                    .append("        disablePasswordAuthentication: false,\n")
                    .append("    },\n")
                    .append("    sourceImageReference: {\n")
                    .append("        publisher: \"").append(vmss.getVirtualMachine().getIdImage().getPublisher()).append("\",\n")
                    .append("        offer: \"").append(vmss.getVirtualMachine().getIdImage().getOffer()).append("\",\n")
                    .append("        sku: \"").append(vmss.getVirtualMachine().getIdImage().getSku()).append("\",\n")
                    .append("        version: \"").append(vmss.getVirtualMachine().getIdImage().getVersion()).append("\",\n")
                    .append("    },\n")
                    .append("});\n");
        }

        architecture.setPulumiCode(pulumiCode.toString());
        architectureRepository.save(architecture);
        return pulumiCode.toString();
    }
    @DeleteMapping("/delete/{id}")
    public void deleteArchitecture(@PathVariable String id) {
        architectureService.deleteArchitecture(id);
    }

    @GetMapping("/archByUser/{userId}")
    public ResponseEntity<List<Architecture>> getUserArchitectures(@PathVariable String userId) {
        List<Architecture> architectures = architectureService.getArchitecturesByUser(userId);
        return ResponseEntity.ok(architectures);
    }

}
