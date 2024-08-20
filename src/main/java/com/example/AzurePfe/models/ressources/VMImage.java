package com.example.AzurePfe.models.ressources;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")

public class VMImage {
    @Id
    private String id;
    private String architecture;
    private String offer;
    private String publisher;
    private String sku;
    private String urn;
    private String urnAlias;
    private String version;

    public String getId() {
        return id;
    }
    public VMImage(String id) {
        this.id = id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getUrnAlias() {
        return urnAlias;
    }

    public void setUrnAlias(String urnAlias) {
        this.urnAlias = urnAlias;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public VMImage(String id, String architecture, String offer, String publisher, String sku, String urn, String urnAlias, String version) {
        this.id = id;
        this.architecture = architecture;
        this.offer = offer;
        this.publisher = publisher;
        this.sku = sku;
        this.urn = urn;
        this.urnAlias = urnAlias;
        this.version = version;
    }

    public VMImage() {
    }

}
