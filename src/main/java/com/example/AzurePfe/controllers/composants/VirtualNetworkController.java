package com.example.AzurePfe.controllers.composants;

import com.example.AzurePfe.models.composant.VirtualNetwork;
import com.example.AzurePfe.security.services.composants.VirtualNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@CrossOrigin(origins = "http://40.83.171.29", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/virtualNetworks")
public class VirtualNetworkController {

    @Autowired
    private VirtualNetworkService virtualNetworkService;

    @PostMapping("/add")
    public VirtualNetwork createVirtualNetwork(@RequestBody VirtualNetwork virtualNetwork) {
        return virtualNetworkService.createVirtualNetwork(virtualNetwork);
    }

    @GetMapping("/get/{id}")
    public VirtualNetwork getVirtualNetwork(@PathVariable String id) {
        return virtualNetworkService.getVirtualNetworkById(id);
    }

    @GetMapping("/getAll")
    public List<VirtualNetwork> getAllVirtualNetworks() {
        return virtualNetworkService.getAllVirtualNetworks();
    }

    @DeleteMapping("/{id}")
    public void deleteVirtualNetwork(@PathVariable String id) {
        virtualNetworkService.deleteVirtualNetwork(id);
    }


}
