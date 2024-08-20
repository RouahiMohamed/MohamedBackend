package com.example.AzurePfe.controllers.composants;

import com.example.AzurePfe.models.composant.Subnet;
import com.example.AzurePfe.security.services.composants.SubnetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@CrossOrigin(origins = "http://40.83.171.29", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/subnets")
public class SubnetController {

    @Autowired
    private SubnetService subnetService;

        @GetMapping("/getAll")
    public List<Subnet> getAllSubnets() {
        return subnetService.getAllSubnets();
    }

    @GetMapping("/getById/{id}")
    public Subnet getSubnetById(@PathVariable String id) {
        return subnetService.getSubnetById(id);
    }

    @PostMapping("/add")
    public Subnet createSubnet(@RequestBody Subnet subnet) {
        return subnetService.createSubnet(subnet);
    }

    @DeleteMapping("/{id}")
    public void deleteSubnet(@PathVariable String id) {
        subnetService.deleteSubnet(id);
    }
}
