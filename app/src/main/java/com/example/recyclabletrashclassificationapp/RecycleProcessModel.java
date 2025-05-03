package com.example.recyclabletrashclassificationapp;

public class RecycleProcessModel {
    private String itemName,itemDescription,imageUrl;

    public RecycleProcessModel() {
        // Required for Firebase or other frameworks (if needed)
    }

    public RecycleProcessModel(String itemName, String itemDescription, String imageUrl) {
        this.itemName = itemName;
        this.itemDescription=itemDescription;
        this.imageUrl=imageUrl;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
