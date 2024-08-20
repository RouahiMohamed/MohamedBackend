package com.example.AzurePfe.security.services.ressources;

import com.example.AzurePfe.models.ressources.VMImage;
import com.example.AzurePfe.repository.ressources.VMImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VMImageService {
    @Autowired
    private VMImageRepository imageRepository;


    public List<VMImage> getAllImages() {
        return imageRepository.findAll();
    }

    public VMImage getImageById(String id) {
        return imageRepository.findById(id).orElse(null);
    }

    public VMImage saveImage(VMImage image) {
        return imageRepository.save(image);
    }

    public void deleteImageById(String id) {
        imageRepository.deleteById(id);
    }

    public VMImage updateImage(String id, VMImage updatedImage) {
        VMImage existingImage = imageRepository.findById(id).orElse(null);
        if (existingImage != null) {
            existingImage.setArchitecture(updatedImage.getArchitecture());
            existingImage.setOffer(updatedImage.getOffer());
            existingImage.setPublisher(updatedImage.getPublisher());
            existingImage.setSku(updatedImage.getSku());
            existingImage.setUrn(updatedImage.getUrn());
            existingImage.setUrnAlias(updatedImage.getUrnAlias());
            existingImage.setVersion(updatedImage.getVersion());
            return imageRepository.save(existingImage);
        }
        return null; // Image non trouvée
    }
}
