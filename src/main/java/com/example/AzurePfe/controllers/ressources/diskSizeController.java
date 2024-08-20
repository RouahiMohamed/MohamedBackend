package com.example.AzurePfe.controllers.ressources;
import com.example.AzurePfe.models.ressources.DiskSize;
import com.example.AzurePfe.security.services.ressources.DiskSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@CrossOrigin(origins = "http://40.83.171.29", maxAge = 3600, allowCredentials = "true")
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")

@RestController
@RequestMapping("/api/diskSizes")
public class diskSizeController {
    @Autowired
    private DiskSizeService diskSizeService;

    @GetMapping("/getAll")
    public List<DiskSize> getAllDiskSizes() {
        return diskSizeService.getAllDiskSizes();
    }

    @GetMapping("/{id}")
    public DiskSize getDiskSizeById(@PathVariable String id) {
        return diskSizeService.getDiskSizeById(id);
    }

    @PostMapping("/addDiskSize")
    public DiskSize saveDiskSize(@RequestBody DiskSize diskSize) {
        return diskSizeService.saveDiskSize(diskSize);
    }

    @DeleteMapping("/{id}")
    public void deleteDiskSizeById(@PathVariable String id) {
        diskSizeService.deleteDiskSizeById(id);
    }
    @GetMapping("/region/{id}")
    public List<DiskSize> getDiskSizesByRegionId(@PathVariable String id) {
        return diskSizeService.getByIdRegion(id);
    }
    @PutMapping("/updateDiskSize/{id}")
    public ResponseEntity<?> updateDiskSize(@PathVariable String id, @RequestBody DiskSize updatedDiskSize) {
        DiskSize diskSize = diskSizeService.updateDiskSize(id, updatedDiskSize);
        if (diskSize != null) {
            return ResponseEntity.ok(diskSize);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}