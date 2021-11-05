package com.example.euleritychanllenge.model;

public class Upload {
    private String appId;
    private String original;
    private Object file;

    public Upload(String appId, String original, Object file) {
        this.appId = appId;
        this.original = original;
        this.file = file;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public Object getFile() {
        return file;
    }

    public void setFile(Object file) {
        this.file = file;
    }
}
