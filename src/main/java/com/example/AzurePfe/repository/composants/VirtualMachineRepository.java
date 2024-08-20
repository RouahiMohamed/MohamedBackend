package com.example.AzurePfe.repository.composants;

import com.example.AzurePfe.models.composant.VirtualMachine;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VirtualMachineRepository extends MongoRepository<VirtualMachine, String> {

}
