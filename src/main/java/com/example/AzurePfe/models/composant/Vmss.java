package com.example.AzurePfe.models.composant;

import com.example.AzurePfe.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vmss")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vmss {
    @Id
    private String id;
    private String name;
    @DBRef
    private VirtualMachine virtualMachine;
    private Integer nb_vm;
    @DBRef
    private User user;

}
