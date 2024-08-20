package com.example.AzurePfe.controllers.composants;

import com.example.AzurePfe.models.composant.ApplicationGateway;
import com.example.AzurePfe.security.services.composants.ApplicationGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@CrossOrigin(origins = "http://40.83.171.29", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/applicationGateways")
public class ApplicationGatewayController {

    @Autowired
    private ApplicationGatewayService applicationGatewayService;

    @PostMapping("/add")
    public ApplicationGateway createApplicationGateway(@RequestBody ApplicationGateway applicationGateway) {
        return applicationGatewayService.createApplicationGateway(applicationGateway);
    }

    @GetMapping("/get/{id}")
    public ApplicationGateway getApplicationGateway(@PathVariable String id) {
        return applicationGatewayService.getApplicationGatewayById(id);
    }

    @GetMapping("/getAll")
    public List<ApplicationGateway> getAllApplicationGateways() {
        return applicationGatewayService.getAllApplicationGateways();
    }

    @DeleteMapping("/{id}")
    public void deleteApplicationGateway(@PathVariable String id) {
        applicationGatewayService.deleteApplicationGateway(id);
    }

    // Additional endpoints can be added here
}
