package com.example.AzurePfe.models.composant;

import com.example.AzurePfe.models.User;
import com.example.AzurePfe.models.ressources.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "resourceGroups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceGroup {
    @Id
    private String id;
    private String name;
    @DBRef
    private Region region;
    @DBRef
    private User user;
}
