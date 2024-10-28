package com.smu.love119.domain.weather.dto;

public class WeatherItem {
    private String category;  // TMP, PCP, REH, SKY, SNO 등
    private String fcstDate;  // 예보 날짜
    private String fcstTime;  // 예보 시간
    private String fcstValue; // 예보 값 (기온, 습도 등)

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFcstDate() {
        return fcstDate;
    }

    public void setFcstDate(String fcstDate) {
        this.fcstDate = fcstDate;
    }

    public String getFcstTime() {
        return fcstTime;
    }

    public void setFcstTime(String fcstTime) {
        this.fcstTime = fcstTime;
    }

    public String getFcstValue() {
        return fcstValue;
    }

    public void setFcstValue(String fcstValue) {
        this.fcstValue = fcstValue;
    }
}
