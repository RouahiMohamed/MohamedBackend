package com.example.AzurePfe.models.composant;

import com.example.AzurePfe.models.User;
import com.example.AzurePfe.models.ressources.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applicationGateway")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationGateway {
    @Id
    private String id;
    private String name;
    private String nameGatewayIpConfiguration;
    private String nameFrontendPort ;
    private String nameFrontendIpconfiguration;
    private String nameBackendHttpSettings;
    private String nameHttpListener;
    private String nameRequestRoutingRule;
    private String namebackendAddressPool;
    @DBRef
    private Region region;
    @DBRef
    private ResourceGroup resourceGroupe;
    @DBRef
    private Subnet subnet;
    @DBRef
    private User user;

}
