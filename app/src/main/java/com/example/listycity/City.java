package com.example.listycity;

public class City {

    private String docId;   // Firestore document ID
    private String name;
    private String province;

    public City() {
    }

    public City(String name, String province) {
        this.name = name;
        this.province = province;
    }

    public City(String docId, String name, String province) {
        this.docId = docId;
        this.name = name;
        this.province = province;
    }
    // Getters
    public String getDocId() {
        return docId;
    }
    public String getName() {
        return name;
    }
    public String getProvince() {
        return province;
    }
    // Setters
    public void setDocId(String docId) {
        this.docId = docId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setProvince(String province) {
        this.province = province;
    }
}