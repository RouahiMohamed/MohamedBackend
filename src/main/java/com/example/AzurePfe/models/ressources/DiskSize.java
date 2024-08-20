package com.example.AzurePfe.models.ressources;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "diskSize")

public class DiskSize {
    @Id
    private String id;
    private int maxDataDiskCount;
    private String idRegion;
    private int memoryInMB;
    private String name;
    private int numberOfCores;
    private int osDiskSizeInMB;
    private int resourceDiskSizeInMB;

    public DiskSize(String id, int maxDataDiskCount, String idRegion, int memoryInMB, String name, int numberOfCores, int osDiskSizeInMB, int resourceDiskSizeInMB) {
        this.id = id;
        this.maxDataDiskCount = maxDataDiskCount;
        this.idRegion = idRegion;
        this.memoryInMB = memoryInMB;
        this.name = name;
        this.numberOfCores = numberOfCores;
        this.osDiskSizeInMB = osDiskSizeInMB;
        this.resourceDiskSizeInMB = resourceDiskSizeInMB;
    }

    public DiskSize() {
    }

    public String getIdRegion() {
        return idRegion;
    }
    public DiskSize(String id) {
        this.id = id;
    }
    public void setIdRegion(String idRegion) {
        this.idRegion = idRegion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxDataDiskCount() {
        return maxDataDiskCount;
    }

    public void setMaxDataDiskCount(int maxDataDiskCount) {
        this.maxDataDiskCount = maxDataDiskCount;
    }

    public int getMemoryInMB() {
        return memoryInMB;
    }

    public void setMemoryInMB(int memoryInMB) {
        this.memoryInMB = memoryInMB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }

    public void setNumberOfCores(int numberOfCores) {
        this.numberOfCores = numberOfCores;
    }

    public int getOsDiskSizeInMB() {
        return osDiskSizeInMB;
    }

    public void setOsDiskSizeInMB(int osDiskSizeInMB) {
        this.osDiskSizeInMB = osDiskSizeInMB;
    }

    public int getResourceDiskSizeInMB() {
        return resourceDiskSizeInMB;
    }

    public void setResourceDiskSizeInMB(int resourceDiskSizeInMB) {
        this.resourceDiskSizeInMB = resourceDiskSizeInMB;
    }
}
