package com.example.classiii;
import java.io.File;

public class ScannedFace {
    public File facefile;
    public String result;
    public String date;
    public String key;
    public String angles;

    public ScannedFace(String key, String date, String result, File facefile, String angles) {
        this.key = key;
        this.result = result;
        this.facefile = facefile;
        this.date = date;
        this.angles = angles;
    }

    public String getDate() {
        return date;
    }

    public String getAngles() {
        return angles;
    }

    public String getKey() {
        return key;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public File getFace() {
        return facefile;
    }

    public void setFace(File face) {
        this.facefile = facefile;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}