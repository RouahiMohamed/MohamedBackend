package com.example.AzurePfe.models.composant;

import com.example.AzurePfe.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "subnet")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subnet {
    @Id
    private String id;
    private String name;
    private String adress;
    @DBRef
    private ResourceGroup resourceGroupe;
    @DBRef
    private Set<VirtualNetwork> virtualNetworks = new HashSet<>();
    @DBRef
    private User user;


}
