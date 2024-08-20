package com.example.AzurePfe.controllers.composants;

import com.example.AzurePfe.models.composant.VirtualMachine;
import com.example.AzurePfe.security.services.composants.VirtualMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@CrossOrigin(origins = "http://40.83.171.29", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/virtualMachines")
public class VirtualMachineController {

    private final VirtualMachineService virtualMachineService;

    @Autowired
    public VirtualMachineController(VirtualMachineService virtualMachineService) {
        this.virtualMachineService = virtualMachineService;
    }

    @GetMapping("/showAllVms")
    public ResponseEntity<List<VirtualMachine>> getAllVirtualMachines() {
        List<VirtualMachine> virtualMachines = virtualMachineService.getAllVirtualMachines();
        return new ResponseEntity<>(virtualMachines, HttpStatus.OK);
    }

    @GetMapping("/showVm/{id}")
    public ResponseEntity<VirtualMachine> getVirtualMachineById(@PathVariable String id) {
        Optional<VirtualMachine> virtualMachine = virtualMachineService.getVirtualMachineById(id);
        return virtualMachine.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/addVm")
    public ResponseEntity<VirtualMachine> createVirtualMachine(@RequestBody VirtualMachine virtualMachine) {
        VirtualMachine savedVirtualMachine = virtualMachineService.saveVirtualMachine(virtualMachine);
        return new ResponseEntity<>(savedVirtualMachine, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteVm/{id}")
    public ResponseEntity<Void> deleteVirtualMachineById(@PathVariable String id) {
        virtualMachineService.deleteVirtualMachineById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

