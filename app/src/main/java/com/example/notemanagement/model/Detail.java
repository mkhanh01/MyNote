package com.example.notemanagement.model;

public class Detail {
    private String name;
    private String createdDate;
    private String email;

    public Detail(String name, String email,String createdDate) {
        this.name = name;
        this.email = email;
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
