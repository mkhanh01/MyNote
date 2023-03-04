package com.example.notemanagement.model;

import java.util.List;

public class BaseResponse {
    private int status;
    private int error;
    private Info info;
    private List<List<String>> data;

    public BaseResponse(int status, int error, Info info, List<List<String>> data) {
        this.status = status;
        this.error = error;
        this.info = info;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}

