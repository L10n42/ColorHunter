package com.example.colorhunter.main.custom_view;

public class CustomColorData {

    private String name, des, hex, rgb;

    public CustomColorData(String name, String des, String hex, String rgb) {
        this.name = name;
        this.des = des;
        this.hex = hex;
        this.rgb = rgb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }
}
