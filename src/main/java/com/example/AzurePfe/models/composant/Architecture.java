package com.example.AzurePfe.models.composant;

import com.example.AzurePfe.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "architecture")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Architecture {
    @Id
    private String id;
    private String name;
    private Date dateCreation;
    @DBRef
    private List<ResourceGroup> resourceGroups;
    @DBRef
    private List<Vmss> vmsses;
    @DBRef
    private List<VirtualMachine> virtualMachines;
    @DBRef
    private List<VirtualNetwork> virtualNetworks;
    @DBRef
    private List<ApplicationGateway> applicationGateways;
    @DBRef
    private List<Subnet> subnets;
    @DBRef
    private User user;
    private String terraformCode;
    private String PulumiCode;
    private String costEstimation;



}
