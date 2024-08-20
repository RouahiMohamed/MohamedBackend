package com.example.AzurePfe.controllers.ressources;

import com.example.AzurePfe.models.ressources.VMImage;
import com.example.AzurePfe.security.services.ressources.VMImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://40.83.171.29", maxAge = 3600, allowCredentials = "true")
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")

@RestController
@RequestMapping("/api/images")
public class VMImageController {
    @Autowired
    private VMImageService imageService;

  @GetMapping("/getAll")
    public List<VMImage> getAllImages() {
        return imageService.getAllImages();
    }

    @GetMapping("/{id}")
    public VMImage getImageById(@PathVariable String id) {
        return imageService.getImageById(id);
    }

    @PostMapping("/addImage")
    public VMImage saveImage(@RequestBody VMImage image) {
        return imageService.saveImage(image);
    }

    @DeleteMapping("/{id}")
    public void deleteImageById(@PathVariable String id) {
        imageService.deleteImageById(id);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateImage(@PathVariable String id, @RequestBody VMImage updatedImage) {
        VMImage image = imageService.updateImage(id, updatedImage);
        if (image != null) {
            return ResponseEntity.ok(image);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
