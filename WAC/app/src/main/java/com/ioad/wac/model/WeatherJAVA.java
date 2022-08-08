package com.ioad.wac.model;

public class WeatherJAVA {

    String max;
    String min;
    String sky;
    String rain;
    String now;
    String fcstTime;

    public WeatherJAVA(String max, String min, String sky, String rain, String now) {
        this.max = max;
        this.min = min;
        this.sky = sky;
        this.rain = rain;
        this.now = now;
    }


    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getSky() {
        return sky;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getFcstTime() {
        return fcstTime;
    }

    public void setFcstTime(String fcstTime) {
        this.fcstTime = fcstTime;
    }
}
