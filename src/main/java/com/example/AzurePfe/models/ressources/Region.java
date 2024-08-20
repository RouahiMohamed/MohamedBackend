package com.example.AzurePfe.models.ressources;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "region")


public class Region {
    @Id
    private String id;
    private String displayName;
    private String name;
    private String regionalDisplayName;
    public Region(String displayName, String name, String regionalDisplayName) {
        this.displayName = displayName;
        this.name = name;
        this.regionalDisplayName = regionalDisplayName;
    }
    public Region(String id) {
        this.id = id;
    }
    public Region() {
    }

    public Region(String id, String displayName, String name, String regionalDisplayName) {
        this.id = id;
        this.displayName = displayName;
        this.name = name;
        this.regionalDisplayName = regionalDisplayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionalDisplayName() {
        return regionalDisplayName;
    }

    public void setRegionalDisplayName(String regionalDisplayName) {
        this.regionalDisplayName = regionalDisplayName;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", name='" + name + '\'' +
                ", regionalDisplayName='" + regionalDisplayName + '\'' +
                '}';
    }
}
