package com.example.notemanagement.model;

public class Note {
    private String name;
    private String category;
    private String priority;
    private String status;
    private String planDate;
    private String createdDate;

    public Note(String name, String category, String priority,
                String status, String planDate, String createdDate) {
        this.name = name;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.planDate = planDate;
        this.createdDate = createdDate;
    }

    public Note(String name, String category, String priority, String status, String planDate) {
        this.name = name;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.planDate = planDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
