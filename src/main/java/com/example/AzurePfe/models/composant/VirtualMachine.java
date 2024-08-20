package com.example.AzurePfe.models.composant;

import com.example.AzurePfe.models.User;
import com.example.AzurePfe.models.ressources.DiskSize;
import com.example.AzurePfe.models.ressources.Region;
import com.example.AzurePfe.models.ressources.VMImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "virtualMachine")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirtualMachine {
    @Id
    private String id;
    private String name;
    @DBRef
    private Region region;
    @DBRef
    private ResourceGroup resourceGroupe;
    @DBRef
    private VMImage idImage;
    @DBRef
    private Subnet subnet;

    private String Username;
    private String Password;
    @DBRef
    private DiskSize idDiskSize;

    @DBRef
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public ResourceGroup getResourceGroupe() {
        return resourceGroupe;
    }

    public void setResourceGroupe(ResourceGroup resourceGroupe) {
        this.resourceGroupe = resourceGroupe;
    }

    public VMImage getIdImage() {
        return idImage;
    }

    public void setIdImage(VMImage idImage) {
        this.idImage = idImage;
    }

    public Subnet getSubnet() {
        return subnet;
    }

    public void setSubnet(Subnet subnet) {
        this.subnet = subnet;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public DiskSize getIdDiskSize() {
        return idDiskSize;
    }

    public void setIdDiskSize(DiskSize idDiskSize) {
        this.idDiskSize = idDiskSize;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
