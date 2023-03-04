package com.example.notemanagement.model;

public class Dashboard {
    private String dashboardName;
    private String dashboardRate;

    public Dashboard(String dashboardName, String dashboardRate) {
        this.dashboardName = dashboardName;
        this.dashboardRate = dashboardRate;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public String getDashboardRate() {
        return dashboardRate;
    }

    public void setDashboardRate(String dashboardRate) {
        this.dashboardRate = dashboardRate;
    }
}
