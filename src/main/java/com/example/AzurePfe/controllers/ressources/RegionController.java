package com.example.AzurePfe.controllers.ressources;

import com.example.AzurePfe.models.ressources.Region;
import com.example.AzurePfe.security.services.ressources.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://40.83.171.29", maxAge = 3600, allowCredentials="true")
//CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")

@RestController
@RequestMapping("/api/Region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @GetMapping("/getAll")
    public List<Region> getAllLocations() {
        return regionService.getAllRegions();
    }

    @GetMapping("/getById/{id}")
    public Region getLocationById(@PathVariable String id) {
        return regionService.getRegionById(id);
    }

    @PostMapping("/addRegion")
    public Region saveLocation(@RequestBody Region Region) {
        return regionService.saveRegion(Region);
    }

    @DeleteMapping("/{id}")
    public void deleteRegionById(@PathVariable String id) {
        regionService.deleteRegionById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRegion(@PathVariable String id, @RequestBody Region updatedRegion) {
        Region region = regionService.updateRegion(id, updatedRegion);
        if (region != null) {
            return ResponseEntity.ok(region);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/name")
    public String getNameById(@PathVariable("id") String id) {
        return regionService.getNameById(id);
    }
}
