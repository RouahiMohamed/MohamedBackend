package com.example.AzurePfe.controllers.composants;

import com.example.AzurePfe.models.composant.Vmss;
import com.example.AzurePfe.security.services.composants.VmssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@CrossOrigin(origins = "http://40.83.171.29", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/vmss")
public class VmssController {

    @Autowired
    private VmssService vmssService;

    @PostMapping("/addVmss")
    public ResponseEntity<Vmss> createVmss(@RequestBody Vmss vmss) {
        Vmss createdVmss = vmssService.createVmss(vmss);
        return new ResponseEntity<>(createdVmss, HttpStatus.CREATED);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Vmss> getVmssById(@PathVariable String id) {
        Vmss vmss = vmssService.getVmssById(id);
        if (vmss != null) {
            return new ResponseEntity<>(vmss, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Vmss>> getAllVmss() {
        List<Vmss> vmssList = vmssService.getAllVmss();
        return new ResponseEntity<>(vmssList, HttpStatus.OK);
    }

    @DeleteMapping("/deleteVmss/{id}")
    public ResponseEntity<Void> deleteVmssById(@PathVariable String id) {
        vmssService.deleteVmssById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
